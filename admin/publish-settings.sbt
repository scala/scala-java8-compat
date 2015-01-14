pgpPassphrase := Some(sys.props("PGP_PASSPHRASE").toArray)

pgpPublicRing := file("admin/pubring.asc")

pgpSecretRing := file("admin/secring.asc")

credentials   += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", sys.props("SONA_USER"), sys.props("SONA_PASS"))
