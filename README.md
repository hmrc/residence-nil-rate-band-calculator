# Residence Nil Rate Band Calculator 

[![Build Status](https://travis-ci.org/hmrc/residence-nil-rate-band-calculator.svg?branch=master)](https://travis-ci.org/hmrc/residence-nil-rate-band-calculator) [ ![Download](https://api.bintray.com/packages/hmrc/releases/residence-nil-rate-band-calculator/images/download.svg) ](https://bintray.com/hmrc/releases/residence-nil-rate-band-calculator/_latestVersion)

### Summary
This service provides RESTful endpoints to calculate the [residence nil-rate band](https://www.gov.uk/guidance/inheritance-tax-residence-nil-rate-band) an estate can qualify for.

### Requirements
This service is written in Scala and Play 3.0, so needs at least a JRE to run.

### Running locally
To run the service locally:

    sbt 'run 7112'

To run dependencies for the service, the following command:

    sm2 --start RNRB_ALL

#### Test Coverage
To run the test coverage suite

`sbt clean coverage test coverageReport`

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


