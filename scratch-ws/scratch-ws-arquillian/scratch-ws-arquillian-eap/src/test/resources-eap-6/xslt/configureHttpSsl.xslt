<?xml version="1.0" encoding="UTF-8"?>
<!-- XSLT file to add the security domains to the standalone.xml used during 
	the integration tests. -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:as="urn:jboss:domain:1.6" 
    xmlns:web="urn:jboss:domain:web:2.1" 
    exclude-result-prefixes="as dom"
	version="1.0">

	<xsl:output method="xml" indent="yes" />

    <xsl:template match="//as:profile/web:subsystem" >
        <subsystem xmlns="urn:jboss:domain:web:2.1" default-virtual-server="default-host" native="false">
            <connector name="https" protocol="HTTP/1.1" scheme="https" socket-binding="https" secure="true">
                <ssl>
                    <xsl:attribute name="name">https</xsl:attribute> 
                    <xsl:attribute name="password">SERVER_KEYSTORE_PASSWORD</xsl:attribute>
                    <xsl:attribute name="certificate-key-file">${jboss.server.config.dir}/ssl/keystore.jks</xsl:attribute>
                </ssl>
            </connector>
            <xsl:apply-templates select="@* | *" />
        </subsystem>
    </xsl:template>
    
	<!-- Copy everything else. -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
