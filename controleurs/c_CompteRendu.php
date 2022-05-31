<?php
include("controleurs/c_Sommaire.php");


$action = $_REQUEST['action'];

if(isset($_SESSION['rap_index']))
{
    $index = $_SESSION['rap_index'];
}
else
{
    $index = 1;
} 

switch($action){ 

    //////////////////// PARTIE SAISIE DU SWTICH /////////////////////

    // Envoie sur la vue saisirCR permettant la saisie d'un CR
    case 'saisirCR':{
        $_SESSION['CR_matricule'] = $pdo->getLeMatriculeVisiteur();
        include("vues/v_saisieCR.php");
	    break;
	}
        
	case 'validerSaisieCR':{
		  
        $Matricule = $_POST['matricule'];
        $Numero=$_POST ['num'];
        $praticien = $_POST['choix_praticien'];
        $praticien = $_POST['choix_lieu'];
        $dateVisite = $_POST['dateVisite'];
        $bilan= $_POST['bilan'];
        $Motif=$_POST['motif'];

        // enlève les espaces
        $Matriculer = str_replace(' ', '', $Matricule);

        $leCR=$pdo->AjouterCR($Matriculer, $Numero, $praticien, $dateVisite, $bilan, $Motif);
        $message = "Compte rendu ajouté avec succès.";
        include("vues/v_Message.php"); 
        break;
	}

    case 'consulterCR':{
        $_SESSION['rap_index'] = 1;
                $index = 1;
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                    foreach($lesComptesRendus as $unCompteRendu)
                    {
                        $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                    }
    
                    
                include("vues/v_consulterCR.php");
                break;  
    }

    
    

    ////////////////////// PARTIE AFFICHAGE DU SWITCH ///////////////////////
  
    case 'suivant':
        {
            $index = $index + 1;
            if ($index <= $pdo->getMaxCompteRendus())
            {
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            else
            {
                $index = $pdo->getMaxCompteRendus();
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            $_SESSION['rap_index'] = $index;
            
            include("vues/v_consulterCR.php");
            break;
        }
    case 'precedent':
        {
            $index = $index - 1;
            if ($index >= 1)
            {
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            else
            {
                $index = 1;
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            $_SESSION['rap_index'] = $index;
            
            include("vues/v_consulterCR.php");
            break;
        }
    case 'custom': 
        {
            $index = $_POST['updateIndex'];
            if ($index >= 1 and $index <= $pdo->getMaxCompteRendus())
            {
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            if ($index < 1)
            {
                $index = 1;
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            if ($index > $pdo->getMaxCompteRendus())
            {
                $index = $pdo->getMaxCompteRendus();
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                foreach($lesComptesRendus as $unCompteRendu)
                {
                    $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                }
            }
            $_SESSION['rap_index'] = $index;
            
            include("vues/v_consulterCR.php");
            break;

            
        }

        default:{
            $_SESSION['rap_index'] = 1;
                $index = 1;
                $lesComptesRendus = $pdo->getLesCompteRendus($index);
                    foreach($lesComptesRendus as $unCompteRendu)
                    {
                        $_SESSION['rap_mat'] = $unCompteRendu['VIS_MATRICULE'];
                    $_SESSION['rap_num'] = $unCompteRendu['RAP_NUM'];
                    $_SESSION['rap_nump'] = $unCompteRendu['PRA_NUM'];
                    $_SESSION['rap_nomp'] = $pdo->getNomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_prenomp'] = $pdo->getPrenomPraticien($_SESSION['rap_nump']);
                    $_SESSION['rap_date'] = $unCompteRendu['RAP_DATE'];
                    $_SESSION['rap_bilan'] = $unCompteRendu['RAP_BILAN'];
                    $_SESSION['rap_motif'] = $unCompteRendu['RAP_MOTIF'];
                    }
    
                    
                include("vues/v_consulterCR.php");
                break;
        }
    }

	


?>