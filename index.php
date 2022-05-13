<?php 
require_once("include/fct.inc.php");
require_once ("include/modele.inc.php");
include("vues/v_entete.php") ;
session_start();
$pdo = PdoGsb::getPdoGsb();
if(!isset($_REQUEST['uc']) || (!isset($_SESSION['login']))){
     $_REQUEST['uc'] = 'connexion';
}	 
$uc = $_REQUEST['uc'];
switch($uc){
	case 'connexion':{
		include("controleurs/c_connexion.php");break;
	}
	case 'gererCR':{
		include("controleurs/c_CompteRendu.php");break;
	}
	case 'medicaments':{
		include("controleurs/c_Medicaments.php");break;
	}
	case 'praticiens':{
		include("controleurs/c_Praticiens.php");break;
	}
	case 'activites':{
		include("controleurs/c_ActivitesComplementaires.php");break;
	}
	default:
	{
		include("controleurs/c_Sommaire.php");
	}
}
include("vues/v_pied.php") ;
?>

