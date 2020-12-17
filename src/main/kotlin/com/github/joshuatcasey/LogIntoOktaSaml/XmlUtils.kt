package com.github.joshuatcasey.LogIntoOktaSaml

import org.opensaml.core.xml.XMLObject
import org.opensaml.core.xml.schema.*
import org.opensaml.saml.saml2.core.Assertion
import java.time.Instant
import java.util.*

fun getAssertionAttributes(assertion: Assertion): Map<String, List<Any>>? {
    val attributeMap: MutableMap<String, List<Any>> = LinkedHashMap()
    for (attributeStatement in assertion.attributeStatements) {
        for (attribute in attributeStatement.attributes) {
            val attributeValues: MutableList<Any> = ArrayList()
            for (xmlObject in attribute.attributeValues) {
                val attributeValue = getXmlObjectValue(xmlObject)
                if (attributeValue != null) {
                    attributeValues.add(attributeValue)
                }
            }
            attributeMap[attribute.name] = attributeValues
        }
    }
    return attributeMap
}


fun getXmlObjectValue(xmlObject: XMLObject): Any? {
    when (xmlObject) {
        is XSAny -> {
            return xmlObject.textContent
        }
        is XSString -> {
            return xmlObject.value
        }
        is XSInteger -> {
            return xmlObject.value
        }
        is XSURI -> {
            return xmlObject.value
        }
        is XSBoolean -> {
            val xsBooleanValue = xmlObject.value
            return xsBooleanValue?.value
        }
        is XSDateTime -> {
            val dateTime = xmlObject.value
            return if (dateTime != null) Instant.ofEpochMilli(dateTime.millis) else null
        }
        else -> return null
    }
}
