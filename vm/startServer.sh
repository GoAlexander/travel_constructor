#!/bin/bash

echo "Script for starting web server for OntologyProject."
echo "Author of script: Alexander Dydychkin (GoAlexander)"

while [[ "$case_number" -ne 3 ]] #is not equal to
do
	echo "   ==============================="
	echo "    1. Start lampp (xampp) server."
	echo "    2. Stop lampp (xampp) server."
	echo "    3. Exit"
	echo "   ==============================="
	read case_number

	if [[ "$case_number" -eq 1 ]]
	then
		sudo /opt/lampp/lampp start
	fi

	if [[ "$case_number" -eq 2 ]]
	then
		sudo /opt/lampp/lampp stop
	fi
done

