#!/bin/bash

echo "Script for startingsome programs for OntologyProject."
echo "Author of script: Alexander Dydychkin (GoAlexander)"

while [[ "$case_number" -ne 3 ]] #is not equal to
do
	echo "   ==============================="
	echo "    1. Start OLED."
	echo "    2. Start Protege."
	echo "    3. Exit"
	echo "   ==============================="
	read case_number

	if [[ "$case_number" -eq 1 ]]
	then
		java -jar ~/Desktop/OLED/oled-v2.0.2.jar
		#java -jar ~/Desktop/CAKE(курсач)/OLED/oled-v2.0.2.jar
	fi

	if [[ "$case_number" -eq 2 ]]
	then
		~/Desktop/Protege-5.0.0-beta-17/run.sh
	fi
done

