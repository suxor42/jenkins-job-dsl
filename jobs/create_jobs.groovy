//@GrabConfig(systemClassLoader=true)
//@Grab(group='org.yaml', module='snakeyaml', version='1.5')

import org.yaml.snakeyaml.Yaml

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
    String applicationame, Map config -> createJobs(applicationame, config)
}

def createJobs(String applicationname, Map config){
    def environments = ['staging', 'prod']
    environments.each {
        String environment -> createJob(environment, applicationname, config)
    }
}

def createJob(String environment, String applicationname, Map config){
    println("$applicationname-$environment: $config")
}
