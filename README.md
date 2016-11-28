# Residence Nil Rate Band Calculator

[![Build Status](https://travis-ci.org/hmrc/residence-nil-rate-band-calculator.svg?branch=master)](https://travis-ci.org/hmrc/residence-nil-rate-band-calculator) [ ![Download](https://api.bintray.com/packages/hmrc/releases/residence-nil-rate-band-calculator/images/download.svg) ](https://bintray.com/hmrc/releases/residence-nil-rate-band-calculator/_latestVersion)

### Summary
This service provides RESTful endpoints to calculate the [residence nil-rate band](https://www.gov.uk/guidance/inheritance-tax-residence-nil-rate-band) an estate can qualify for.

### Requirements
This service is written in Scala and Play 2.5, so needs at least a JRE to run.

### Running locally
To run the service locally:

    sbt 'run 7112'

### Running the RAML console
To get the RAML console up and running:
1. Clone the github repo

    git clone git@github.com:mulesoft/api-console.git
    
2. Move to the working directory

    cd api-console/dist
    
3. Start a simple web server

    python -m SimpleHTTPServer 9000

4. Navigate to localhost:9000 in your browser and you can now interact with the service

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
