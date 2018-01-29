# test2
test2 sanitas

Para añadir el artefacto sportalclientesweb lo hemos realizado mediante Import --> Maven --> Install or deploy an artifact to a maven repository

Para un rápido arreglo del análisis de código Sonar hemos instalado en local Sonar y el plugin de SonarQube en el eclipse para que detecte online, y de manera rápida las alertas sonar, para ello:
  - Hemos Arrancado el Sonar en local en el puerto por defecto http://localhost:9000 para enlazarlo con el plugin sonarqube de eclipse
  - La instalación del plugin de sonar la hemos realizado desde Install New Software.
	  http://downloads.sonarsource.com/eclipse/eclipse/)

Como posible añadido para desarrollar cara a vulnerabilidades de ciber seguridad es posible incorporar un plugin de kiuwan en eclipse para mayor agilidad en el desarrollo, para de esta manera no tener vulnerabilidades en la aplicación y obtener el mayor rango de estrellas que es como puntua kiuwan o fortify, apps destinadas a la medición de la calidad y medida de seguridad de las mismas.

Para compilar el proyecto con maven, "mvn clean install", se ejecutaran 2 test unitarios.

Para ver los resultados de Jacoco en Sonar, ejecutar los comandos "mvn sonar:sonar" y se podrá ver la gráfica con la cobertura de test del proyecto test2 en sonar.