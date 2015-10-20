import groovy.json.JsonSlurper
import org.yaml.snakeyaml.Yaml

/*
When running inside jenkins the script tries to access /resources
When running via gradle test the scripts tries to access ${project.root}/resources
*/
def file
try {
    file = new File("${WORKSPACE}", 'resources/test_data.json')
} catch (groovy.lang.MissingPropertyException exception) {
    file = new File('resources/test_data.json')
}

def yaml = new Yaml()
def JsonSlurper jsonParser = new JsonSlurper();
println(file.canonicalPath)
def object = jsonParser.parseText(file.text)
assert object instanceof Map



object.environment.each {
    String environmentname, Map config ->
        createJobs(environmentname, config, object.applications.keySet() as List)
}

def createJobs(String environmentname, Map config, List applications){
    applications.each {
        application -> createJob(environmentname, application, config)
    }
}

def createJob(String environment, String applicationname, Map config){

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
