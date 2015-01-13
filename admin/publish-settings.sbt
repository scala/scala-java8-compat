pgpPassphrase := Some(sys.prop("PGP_PASSPHRASE").toArray)

pgpPublicRing := file("admin/pubring.asc")

pgpSecretRing := file("admin/secring.asc")

credentials   += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", sys.prop("SONA_USER"), sys.prop("SONA_PASS"))
