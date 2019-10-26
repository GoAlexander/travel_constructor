#!/bin/bash

echo "This script is only for the mainainer of VM!"
echo "Author of script: Alexander Dydychkin (GoAlexander)"

#install  git + open-jdk
sudo apt-get install git default-jdk

#git clone the repo
mkdir ~/workspace
cd ~/workspace
git clone https://gitlab.com/GoAlexander/Ontology.git
cd .. 

#TODO more automatization...
#...

#install virtualbox guest extensions
sudo apt-get install virtualbox-guest-dkms virtualbox-guest-utils virtualbox-guest-x11

echo "Need reboot of VM..."

sudo reboot

