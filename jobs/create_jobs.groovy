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
def json = jsonParser.parseText(file.text)
assert json instanceof Map


json.each {
    String environmentname, Map config ->
        println(environmentname)
        createJobs(environmentname, config.applications as Map)
}

def createJobs(String environmentname, Map applications){
    applications.each {
        application, configuration ->
            println(application)
            createJob(environmentname, application, configuration.config)
    }
}

def createJob(String environment, String applicationname, Map config){

    job("deployment-$applicationname-$environment") {
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
