package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Entity
data class Customer(var name: String? = null,
                    var email: String? = null,
                    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                    var id: Long? = null)

interface CustomerRepository : CrudRepository<Customer, Long>

@Service
class CustomerService(
        private val customerRepository: CustomerRepository
) {

    @Transactional
    fun writeToDbAndExternalSystem() {
        val customer = customerRepository.save(Customer("ali"))
        customer.email = getCustomerEmailFromExternalSystem()
        customerRepository.save(customer)
    }

    fun list(): Iterable<Customer> = customerRepository.findAll()

    private fun getCustomerEmailFromExternalSystem(): String {
        Thread.sleep(10 * 1000)
        return "ali@example.com"
    }
}

@RestController
class CustomerController(
        private val customerService: CustomerService
) {
    @PostMapping
    fun createNew() = customerService.writeToDbAndExternalSystem()

    @GetMapping
    fun list(): Iterable<Customer> = customerService.list()
}
