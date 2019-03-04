job('antbuild') {
    scm {
        github('tetradev01/demoapp', 'master')
    }

    steps {
        ant {
            prop('version', 'dev')
            buildFile('build.xml')
            antInstallation('Ant 1.9.3')
        }
    }

    publishers {
        downstream 'deploy', 'SUCCESS'
    }
}

job('deploy') {
    description 'Deploy app to the demo application server'
    /*
     * configuring ssh plugin to run docker commands
     */
    steps{
             shell 'cp /opt/jenkins/workspace/antbuild/build/demoapp-dev.war /opt/tomcat/latest/webapps/'
      }
}
