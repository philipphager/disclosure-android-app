Disclosure Android App
======================

[![Build Status](https://travis-ci.org/philipphager/disclosure-android-app.svg?branch=master)](https://travis-ci.org/philipphager/disclosure-android-app)

An Android app to detect third-party library usages in other installed apps
and an analyzer to detect permission usages inside the libraries.

Android apps share their permissions with all third-party libraries which they use.
This fact can be exploited and abused by libraries. Especially ad-libraries and
frameworks have been observed to silently probe at runtime, which permissions they
 can access through their host application to gain more user information.
Since not all libraries document and declare their permission usages honestly,
this app provides a tool to gain more insight into the permission usages of
third-party libraries.

Therefore the Disclosure app is a static code analyzer, searching for
permission-protected method calls to the Android API inside libraries.
It also shows, if a found permission can actually be used or if the
host application does not hold the permission.

The app comes with a database of over 30.000 API methods, which are permission-
protected. The tool used to generate them is [PScout](https://github.com/philipphager/pscout-legacy-parser).
The libraries, which are detected are retrieved from a central Disclosure library
database. As this app is in demo, only around 250 third-party libraries are detected,
mainly from advertising or analytics categories. **If you want to analyze permissions
of a library that is currently not in the central database, you can add it locally
via the app.**


![Demo GIF](disclosure-demo.gif)

## Disclaimer
**The Disclosure app operates completely on the device. No detected libraries
or any other data is synced or tracked online. Every thing is local and can be
completely deleted.**

