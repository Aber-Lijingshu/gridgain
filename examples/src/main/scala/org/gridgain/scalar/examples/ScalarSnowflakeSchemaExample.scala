package org.gridgain.scalar.examples

import org.gridgain.scalar.scalar
import org.gridgain.scalar.scalar._
import scala.collection.JavaConversions._
import org.gridgain.grid.cache.GridCacheProjection
import scala.StringBuilder
import org.gridgain.grid.util.typedef.internal.A
import java.util.ConcurrentModificationException
import java.util
import org.jdk8.backport.ThreadLocalRandom8

/**
 * <a href="http://en.wikipedia.org/wiki/Snowflake_schema">Snowflake Schema</a> is a logical
 * arrangement of data in which data is split into `dimensions`  and `facts`
 * <i>Dimensions</i> can be referenced or joined by other <i>dimensions</i> or <i>facts</i>,
 * however, <i>facts</i> are generally not referenced by other facts. You can view <i>dimensions</i>
 * as your master or reference data, while <i>facts</i> are usually large data sets of events or
 * other objects that continuously come into the system and may change frequently. In GridGain
 * such architecture is supported via cross-cache queries. By storing <i>dimensions</i> in
 * `GridCacheMode#REPLICATED REPLICATED` caches and <i>facts</i> in much larger
 * `GridCacheMode#PARTITIONED PARTITIONED` caches you can freely execute distributed joins across
 * your whole in-memory data grid, thus querying your in memory data without any limitations.
 * <p>
 * In this example we have two <i>dimensions</i>, `DimProduct` and `DimStore` and
 * one <i>fact</i> - `FactPurchase`. Queries are executed by joining dimensions and facts
 * in various ways.
 * <p>
 * Remote nodes should always be started with configuration file which includes
 * cache: `'ggstart.sh examples/config/example-cache.xml'`.
 */
object ScalarSnowflakeSchemaExample {
    /** ID generator. */
    private[this] val idGen = Stream.from(0).iterator

    /**
     * Example entry point. No arguments required.
     */
    def main(args: Array[String]) {
        scalar("examples/config/example-cache.xml") {
            populateDimensions()
            populateFacts()

            queryStorePurchases()
            queryProductPurchases()
        }
    }

    /**
     * Populate cache with `dimensions` which in our case are
     * `DimStore` and `DimProduct` instances.
     */
    def populateDimensions() {
        val dimCache = grid$.cache[Int, Object]("replicated")

        val store1 = new DimStore(idGen.next(), "Store1", "12345", "321 Chilly Dr, NY")
        val store2 = new DimStore(idGen.next(), "Store2", "54321", "123 Windy Dr, San Francisco")

        // Populate stores.
        dimCache.put(store1.id, store1)
        dimCache.put(store2.id, store2)

        for (i <- 1 to 20) {
            val product = new DimProduct(idGen.next(), "Product" + i, i + 1, (i + 1) * 10)

            dimCache.put(product.id, product)
        }
    }

    /**
     * Populate cache with `facts`, which in our case are `FactPurchase` objects.
     */
    def populateFacts() {
        val dimCache = grid$.cache[Int, Object]("replicated")
        val factCache = grid$.cache[Int, FactPurchase]("partitioned")

        val stores: GridCacheProjection[Int, DimStore] = dimCache.viewByType(classOf[Int], classOf[DimStore])
        val prods: GridCacheProjection[Int, DimProduct] = dimCache.viewByType(classOf[Int], classOf[DimProduct])

        for (i <- 1 to 100) {
            val store: DimStore = rand(stores.values)
            val prod: DimProduct = rand(prods.values)
            val purchase: FactPurchase = new FactPurchase(idGen.next(), prod.id, store.id, (i + 1))

            factCache.put(purchase.id, purchase)
        }
    }

    /**
     * Query all purchases made at a specific store. This query uses cross-cache joins
     * between `DimStore` objects stored in `replicated` cache and
     * `FactPurchase` objects stored in `partitioned` cache.
     */
    def queryStorePurchases() {
        val factCache = grid$.cache[Int, FactPurchase]("partitioned")

        val storePurchases = factCache.sql(
            "from \"replicated\".DimStore, \"partitioned\".FactPurchase " +
            "where DimStore.id=FactPurchase.storeId and DimStore.name=?", "Store1")

        printQueryResults("All purchases made at store1:", storePurchases)
    }

    /**
     * Query all purchases made at a specific store for 3 specific products.
     * This query uses cross-cache joins between `DimStore`, `DimProduct`
     * objects stored in `replicated` cache and `FactPurchase` objects
     * stored in `partitioned` cache.
     */
    private def queryProductPurchases() {
        val dimCache = grid$.cache[Int, Object]("replicated")
        val factCache = grid$.cache[Int, FactPurchase]("partitioned")

        val prods: GridCacheProjection[Int, DimProduct] = dimCache.viewByType(classOf[Int], classOf[DimProduct])

        val p1: DimProduct = rand(prods.values)
        val p2: DimProduct = rand(prods.values)
        val p3: DimProduct = rand(prods.values)

        println("IDs of products [p1=" + p1.id + ", p2=" + p2.id + ", p3=" + p3.id + ']')

        val prodPurchases = factCache.sql(
            "from \"replicated\".DimStore, \"replicated\".DimProduct, \"partitioned\".FactPurchase " +
            "where DimStore.id=FactPurchase.storeId and " +
                "DimProduct.id=FactPurchase.productId and " +
                "DimStore.name=? and DimProduct.id in(?, ?, ?)",
            "Store2", p1.id, p2.id, p3.id)

        printQueryResults("All purchases made at store2 for 3 specific products:", prodPurchases)
    }

    /**
     * Print query results.
     *
     * @param msg Initial message.
     * @param res Results to print.
     */
    private def printQueryResults[V](msg: String, res: Iterable[(Int, V)]) {
        println(msg)

        for (e <- res)
            println("    " + e.getValue.toString)
    }

    /**
     * Gets random value from given collection.
     *
     * @param c Input collection (no `null` and not emtpy).
     * @return Random value from the input collection.
     */
    def rand[T](c: util.Collection[_ <: T]): T = {
        val n: Int = ThreadLocalRandom8.current.nextInt(c.size)

        var i: Int = 0

        for (t <- c) {
            if (i < n)
                i += 1
            else
                return t
        }

        throw new ConcurrentModificationException
    }
}

/**
 * Represents a physical store location. In our `snowflake` schema a `store`
 * is a `dimension` and will be cached in `GridCacheMode#REPLICATED` cache.
 *
 * @param id Primary key.
 * @param name Store name.
 * @param zip Zip code.
 * @param addr Address.
 */
class DimStore(
    @ScalarCacheQuerySqlField(unique = true)
    val id: Int,
    @ScalarCacheQuerySqlField
    val name: String,
    val zip: String,
    val addr: String) {
    /**
     * `toString` implementation.
     */
    override def toString: String = {
        val sb: StringBuilder = new StringBuilder

        sb.append("DimStore ")
        sb.append("[id=").append(id)
        sb.append(", name=").append(name)
        sb.append(", zip=").append(zip)
        sb.append(", addr=").append(addr)
        sb.append(']')

        sb.toString()
    }
}

/**
 * Represents a product available for purchase. In our `snowflake` schema a `product`
 * is a `dimension` and will be cached in `GridCacheMode#REPLICATED` cache.
 *
 * @param id Product ID.
 * @param name Product name.
 * @param price Product list price.
 * @param qty Available product quantity.
 */
class DimProduct(
    @ScalarCacheQuerySqlField(unique = true)
    val id: Int,
    val name: String,
    @ScalarCacheQuerySqlField
    val price: Float,
    val qty: Int) {
    /**
     * `toString` implementation.
     */
    override def toString: String = {
        val sb: StringBuilder = new StringBuilder

        sb.append("DimProduct ")
        sb.append("[id=").append(id)
        sb.append(", name=").append(name)
        sb.append(", price=").append(price)
        sb.append(", qty=").append(qty)
        sb.append(']')

        sb.toString()
    }
}

/**
 * Represents a purchase record. In our `snowflake` schema purchase
 * is a `fact` and will be cached in larger `GridCacheMode#PARTITIONED` cache.
 *
 * @param id Purchase ID.
 * @param productId Purchased product ID.
 * @param storeId Store ID.
 * @param purchasePrice Purchase price.
 */
class FactPurchase(
    @ScalarCacheQuerySqlField
    val id: Int,
    @ScalarCacheQuerySqlField
    val productId: Int,
    @ScalarCacheQuerySqlField
    val storeId: Int,
    @ScalarCacheQuerySqlField
    val purchasePrice: Float) {
    /**
     * `toString` implementation.
     */
    override def toString: String = {
        val sb: StringBuilder = new StringBuilder

        sb.append("FactPurchase ")
        sb.append("[id=").append(id)
        sb.append(", productId=").append(productId)
        sb.append(", storeId=").append(storeId)
        sb.append(", purchasePrice=").append(purchasePrice)
        sb.append(']')

        sb.toString()
    }
}
