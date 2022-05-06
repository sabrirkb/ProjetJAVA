<?php
include("vues/v_sommaire.php");


$action = $_REQUEST['action'];
switch($action){ 
    // Envoie sur la vue saisirCR permettant la saisie d'un CR
    case 'saisirCR':{
	    // include("vues/v_sommaire.php");
        $lesPraticiens=$pdo->getLesPraticiens();
        $lesProduits=$pdo->getLesProduits();
        include("vues/v_saisieCR.php");
	    break;
	}
        
	case 'validerSaisieCR':{
		// include("vues/v_sommaire.php");   
        $Matricule = $_POST['matricule'];
        $Numero= $_POST['numVisite'];
        $dateVisite = $_POST['dateVisite'];
        $praticien = $_POST['choix_praticien'];
        $coef = $_POST['coef'];
        $remplacant = $_POST['remplacant'];
        $Motif=$_POST['motif'];
        $Echantillon= $_POST['nbreEchantillon'];
        $Produit= $_POST['documentation'];
        $bilan= $_POST['choix_produit'];
        $documentation= $_POST['documentation'];

        $leCR=$pdo->AjouterCR($Matricule, $Numero, $dateVisite, $praticien, $coef, $remplacant , $Motif, $Echantillon, $Produit, $bilan, $documentation);
        $message = "Compte rendu ajouté avec succès";
       // include("vues/v_Message.php"); 
        break;
	}
  
	}


?>