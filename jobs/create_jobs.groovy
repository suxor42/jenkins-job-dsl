import org.yaml.snakeyaml.Yaml

/*
When running inside jenkins the script tries to access /resources
When running via gradle test the scripts tries to access ${project.root}/resources
*/
def file
try {
    file = new File("${WORKSPACE}", 'resources/test_data.yml')
} catch (groovy.lang.MissingPropertyException exception) {
    file = new File('resources/test_data.yml')
}

def yaml = new Yaml()
println(file.canonicalPath)
def object = yaml.load(file.text)
assert object instanceof Map



object.applications.each {
    String applicationame, Map config ->
        createJobs(applicationame, config, object.environments.keySet() as List)
}

def createJobs(String applicationname, Map config, List environments){
    environments.each {
        environment -> createJob(environment, applicationname, config)
    }
}

def createJob(String environment, String applicationname, Map config){
    println("$applicationname-$environment: $config")

    job("$applicationname-$environment") {
        scm {
            git("https://github.com/as-ideas/yana-$applicationname")
        }
        triggers {
            scm('*/1 * * * *')
        }
        steps {
            shell("eb deploy $environment")
        }
    }


}
