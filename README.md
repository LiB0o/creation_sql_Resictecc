# Creation_SQL_Resictecc
 Creation_SQL_Resictecc est un script Java qui permet de remplir une base de données PHPMyAdmin pour le projet Resitecc au niveau du module SAMU/SMUR

 ## Installation
 Télécharger l'archive et compiler le projet. 

 ## Usage
Après avoir installé l'archive, allez où se trouvent les .class (réalisés lors de la compilation), puis lancez la commande " ../../jdk-26_linux-x64_bin/jdk-26.0.1/bin/java -cp ".:assets/poi-bin-5.2.3/*:assets/poi-bin-5.2.3/lib/*:assets/poi-bin-5.2.3/ooxml-lib/*:assets/mysql-connector-j-9.7.0/*:assets/*" Main [Url compléte de la base de donnée] [login administrator] [date de l'exercice] [mot de passe administrator si il n'est pas vide] "

[Url compléte de la base de donnée] : URL complète vers la base de données (https:// à ne pas mettre)

[login administrator] : Nom du compte administrateur pour modifier la base de données

[date de l'exercice] : Format attendu "Année-Mois-Jour"

[mot de passe administrator si il n'est pas vide] : Mot de passe utilisé pour se connecter au compte administrateur pour modifier la base de données

 ## Modifications possibles

Si vous souhaitez rajouter/retirer des options dans l'insertion (plus ou moins de noms/prénoms différents par exemple), vous pouvez modifier le fichier Excel dans le dossier "assets"
Si vous modifiez le dossier, faites attention à quelques points :
 * Ne laisser pas de lignes vides entre vos nouvelles insertions
 * Ne rajouter/n'enlever pas de colonnes
