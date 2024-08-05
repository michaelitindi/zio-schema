package zio.schema.codec

import zio.schema._
import zio.schema.codec.JsonCodec._
import zio.test.Assertion._
import zio.test._

object JsonCodecSpec extends DefaultRunnableSpec {

  final case class Person(name: String, age: Int, address: Address)
  final case class Address(street: String, city: String)

  val personSchema: Schema[Person] = DeriveSchema.gen[Person]
  val addressSchema: Schema[Address] = DeriveSchema.gen[Address]

  def spec = suite("JsonCodecSpec")(
    suite("JSON decoding")(
      test("should decode JSON correctly when ADT field is not the last field in the case class") {
        val json = """{"name":"John","address":{"street":"123 Main St","city":"Anytown"},"age":30}"""
        val decoded = decode(personSchema, json)
        assert(decoded)(isRight(equalTo(Person("John", 30, Address("123 Main St", "Anytown")))))
      },
      test("should decode JSON correctly when ADT field is the last field in the case class") {
        val json = """{"name":"John","age":30,"address":{"street":"123 Main St","city":"Anytown"}}"""
        val decoded = decode(personSchema, json)
        assert(decoded)(isRight(equalTo(Person("John", 30, Address("123 Main St", "Anytown")))))
      }
    )
  )
}