@echo off
set "JS_SCRIPT_PATH=./index.js"
set "NODE_PATH=C:\chemin\vers\node.exe"

"%NODE_PATH%" "%JS_SCRIPT_PATH%"



Enregistrez le code  ci-dessus avec une extension .bat, par exemple, run_script.bat.

Maintenant, pour planifier l'exécution toutes les 24 heures, vous pouvez utiliser le planificateur de tâches de Windows. Suivez ces étapes :

a. Appuyez sur Win + S, tapez "Planificateur de tâches" et appuyez sur Entrée pour ouvrir le Planificateur de tâches.

b. Dans le volet Actions, cliquez sur "Créer une tâche de base".

c. Suivez les étapes du créateur de tâches, en spécifiant la fréquence d'exécution à toutes les 24h.

d. Dans l'onglet "Actions", ajoutez une nouvelle action avec le chemin complet vers votre fichier batch (run_script.bat).

e. Terminez le processus de création de la tâche.