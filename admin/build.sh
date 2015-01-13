#!/bin/bash

# prep environment for publish to sonatype staging if the HEAD commit is tagged

# git on travis does not fetch tags, but we have TRAVIS_TAG
# headTag=$(git describe --exact-match ||:)

if [ "$TRAVIS_JDK_VERSION" == "$PUBLISH_JDK" ] && [[ "$TRAVIS_TAG" =~ ^v[0-9]+\.[0-9]+\.[0-9]+(-[A-Za-z0-9-]+)? ]]; then
  echo "Going to release from tag $TRAVIS_TAG!"
  myVer=$(echo $TRAVIS_TAG | sed -e s/^v// | sed -e 's/_[0-9]*\.[0-9]*//')
  publishVersion='set every version := "'$myVer'"'
  extraTarget="publish-signed"
  cat admin/gpg.sbt >> project/plugins.sbt
  cp admin/publish-settings.sbt .

  # Copied from the output of genKeyPair.sh
  K=$encrypted_1ce132863fa7_key
  IV=$encrypted_1ce132863fa7_iv

  aes-256-cbc -K $K -iv $IV -in admin/secring.asc.enc -out admin/secring.asc -d
fi

sbt ++$TRAVIS_SCALA_VERSION "$publishVersion" clean update test publishLocal $extraTarget
