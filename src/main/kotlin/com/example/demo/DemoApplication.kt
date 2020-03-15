package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
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
data class Audit(var msg: String? = null,
                 val start: Instant = Instant.now(),
                 var stop: Instant? = null,
                 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                 var id: Long? = null) {
    fun end() = this.apply { stop = Instant.now() }
}

interface AuditRepository : CrudRepository<Audit, Long>

@Service
class BillingService(private val auditRepository: AuditRepository) {

    @Transactional
    fun createCustomer() {
        val audit = auditRepository.save(Audit("calling billing webservice"))
        callBillingWebService()
        auditRepository.save(audit.end())
    }

    private fun callBillingWebService() {
        Thread.sleep(10 * 1000)
    }
}

@Service
class AuditService(private val auditRepository: AuditRepository) {

    fun list() = auditRepository.findAll()
}

@RestController
class BillingController(
        private val auditService: AuditService,
        private val billingService: BillingService
) {
    // curl -X POST localhost:8080/billing/customer
    @PostMapping("/billing/customer")
    fun createNew() = billingService.createCustomer()

    // curl localhost:8080/audit
    @GetMapping("/audit")
    fun getAudit() = auditService.list()
}
