job('nodejs-dsl-checkout') {
    
    scm {
        github('tetradev01/nodejsapp', 'master')
    }
  
   publishers {
        downstream 'nodejs-dsl-install', 'SUCCESS'
    }
}

job('nodejs-dsl-install') {
	customWorkspace('/opt/jenkins/workspace/nodejs-dsl-checkout')

	steps{
		shell 'sudo npm install'
	}

	publishers{
		downstream 'nodejs-dsl-test', 'SUCCESS'
	}
}

job('nodejs-dsl-test'){
	customWorkspace('/opt/jenkins/workspace/nodejs-dsl-checkout')

	steps{
		shell 'npm start'
		shell 'npm test'
		shell 'npm stop'
	}

	publishers{
		downstream 'nodejs-dsl-archive', 'SUCCESS'
	}
}


job('nodejs-dsl-archive'){
	configure { project ->
        project / buildWrappers / 'org.jvnet.hudson.plugins.SSHBuildWrapper' {
            siteName 'vagrant@ubuntu1:22'
            postScript """        
            	tar -zcvf /var/archive/app.tar.gz /var/myapp/           
	      """
            }
	}

	publishers{
		downstream 'nodejs-dsl-deploy', 'SUCCESS'
	}
}

job('nodejs-dsl-deploy'){
	configure { project ->
        project / buildWrappers / 'org.jvnet.hudson.plugins.SSHBuildWrapper' {
            siteName 'vagrant@ubuntu1:22'
            postScript """        
            	cd /var/myapp
            	git pull origin master
				"""
            }
	}
    }
