The MIT License (MIT)

Copyright (c) 2013 Andreas Alanko, Emil Nilsson, Fredrik Larsson,
Joakim Strand, Sandra Fridälv.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

# SGPM
## Introduction
SGPM is a tool for visualizing code review data taken from a Gerrit server 
and is developed as a student project for Sony Mobile.

Please note that the product is a prototype and might not be fit for production use.


## Getting started
SGPM is developed using [Grails] and can be deployed as a WAR file with e.g. an
[Apache Tomcat][tomcat] server.

See documentation for your server on how to deploy a WAR file.


### Building the project
The project can be built by running `grails war` from the project root directory.
This will create a WAR-file in the `target` directory.

See Grails documentation on how to [deploy an application][grailsdeploy] and on
the [`grails war` command][grailswar] for more information.


### Deploying under Tomcat
Deploy SGPM by moving the WAR file to the `webapps` directory.

SGPM is configured from `sgpm.properties` in the `conf` directory for the [Tomcat] server.


### Configuration
The configuration is loaded at server startup and the server must subsequently be restarted after
the configuration file has been modified.
The configuration file is a Java Properties file.

The following parameters can be set in the configuration file:

* `sgpm.gerrit.url`: The URL of the Gerrit review server (defaults to `http://127.0.0.1`).
* `sgpm.gerrit.username`: The username for the Gerrit account used to fetch data.
* `sgpm.gerrit.password`: The password for the Gerrit account used to fetch data.
* `sgpm.gerrit.proxy.url`: The URL of a proxy server used to access the Gerrit Review server.
* `sgpm.gerrit.proxy.port`: The port of the proxy server (defaults to `80`).
* `sgpm.gerrit.proxy.scheme`: The scheme of the proxy server (defaults to `http`).

The Gerrit account must be able to fetch change information for the projects used in SGPM using the REST API.

A proxy server will be used if the `sgpm.gerrit.proxy.url` parameter is set.

#### Example configuration:
    
    // Server URL
    sgpm.gerrit.url = http://code-review.example.org
    // Gerrit account username
    sgpm.gerrit.username = sgpm-bot
    // Gerrit account password
    sgpm.gerrit.password = secret


# Database configuration
SGPM uses a database accessed using JDBC with the Hibernate plugin included in Grails.

By default SGPM uses a built-in [H2] database which requires no set up.
If you need to use another database this can be configured by changing `DataSource.groovy`
in `grails-app/conf`.
Please see the Grails documentation on how to [configure the data source][dbconf].

# Licensing
SGPM is released under the MIT License.

SGPM includes the [Highcharts JS Library][highcharts] licensed under
[Creative Commons Attribution NonCommercial 3.0 License][ccbync30].

SGPM may be distributed and used non-commercially bundled with
Highcharts JS (see [here][hcredistribute]), however Highcharts is *not*
free for commercial use under this license.
See [this page](http://shop.highsoft.com/highcharts.html) if you're
interested in buying a commercial license for [Highcharts].


[grails]:           http://grails.org/
[tomcat]:           http://tomcat.apache.org/
[grailsdeploy]:     http://grails.org/doc/latest/guide/gettingStarted.html#deployingAnApplication
[grailswar]:        http://grails.org/doc/latest/ref/Command%20Line/war.html
[h2]:               http://www.h2database.com/html/main.html
[dbconf]:           http://grails.org/doc/latest/guide/conf.html#dataSource
[highcharts]:       http://www.highcharts.com/
[hcredistribute]:   http://shop.highsoft.com/faq#non-commercial-redistribution
[ccbync30]:         http://creativecommons.org/licenses/by-nc/3.0/
