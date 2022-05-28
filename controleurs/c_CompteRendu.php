<?php
include("controleurs/c_Sommaire.php");


$action = $_REQUEST['action'];
switch($action){ 
    // Envoie sur la vue saisirCR permettant la saisie d'un CR
    case 'saisirCR':{
	    // include("controleurs/c_Sommaire.php.php");
        // $lesPraticiens=$pdo->getPraticiens();
        //$lesProduits=$pdo->getLesProduits();
        include("vues/v_saisieCR.php");
	    break;
	}
        
	case 'validerSaisieCR':{
		// include("controleurs/c_Sommaire.php.php");   
        $Matricule = $_POST['matricule'];
        $Numero=$_POST ['num'];
        $praticien = $_POST['choix_praticien'];
        $dateVisite = $_POST['dateVisite'];
        $bilan= $_POST['bilan'];
        $Motif=$_POST['motif'];

        // enlève les espaces
        $Matriculer = str_replace(' ', '', $Matricule);

        $leCR=$pdo->AjouterCR($Matriculer, $Numero, $praticien, $dateVisite, $bilan, $Motif);
        $message = "Compte rendu ajouté avec succès";
        include("vues/v_Message.php"); 
        break;
	}

    case 'consulterCR':{
        include("vues/v_consulterCR.php");
        break;
    }
    
  
	}


?>