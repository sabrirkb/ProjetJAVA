<?php

/*Ce controleur permet de colorer la page de navigation active dans le sommaire*/

if(!isset($_REQUEST['page']))
{
	$_REQUEST['page'] = 'default';
}
$page = $_REQUEST['page'];

// variables pour surbrillance menu (class+=" active")
      $_SESSION['activerSmenu1'] = "";
      $_SESSION['activerSmenu2'] = "";
      $_SESSION['activerSmenu3'] = "";
      $_SESSION['activerSmenu4a'] = "";
      $_SESSION['activerSmenu4b'] = "";
      $_SESSION['activerSmenu5'] = "";
      $_SESSION['activerSmenu6'] = "";
      $_SESSION['activerSmenu7'] = "";

// variables pour cacher/afficher sous-menu (<... 'hidden'> pour cacher <... ''> pour afficher)
      $_SESSION['afficherSmenu1'] = '';
      $_SESSION['afficherSmenu2'] = '';
      $_SESSION['afficherSmenu3'] = '';
      $_SESSION['afficherSmenu4a'] = '';
      $_SESSION['afficherSmenu4b'] = '';
      $_SESSION['afficherSmenu5'] = '';
      $_SESSION['afficherSmenu6'] = '';


switch($page)
{
    case("1"):
        {
            $_SESSION['activerSmenu1'] = "active";
            break;
        }
        case("2"):
        {
            $_SESSION['activerSmenu2'] = "active";
            break;
        }
        case("3"):
            {
               $_SESSION['activerSmenu3'] = "active";
                break;
            }
            case("4a"):
                {
                  $_SESSION['activerSmenu4a'] = "active";
                    break;
                }
                case("4b"):
                  {
                     $_SESSION['activerSmenu4b'] = "active";
                      break;
                  }
                case("5"):
                    {
                     $_SESSION['activerSmenu5'] = "active";
                        break;
                    }
                    case("6"):
                     { 
                        $_SESSION['activerSmenu6'] = "active";
                        break;
                     }
                     case("7"):
                        { 
                           $_SESSION['activerSmenu7'] = "active";
                           break;
                        }
    default:
    {
        echo "";
        break;
    }
}

$_REQUEST['role_util'] = $_SESSION['role_vis'];

if(!isset($_REQUEST['role_util']))
{
	$_REQUEST['role_util'] = 'default';
}
$role_util = $_REQUEST['role_util'];

switch($role_util)
{
   case("Visiteur"):{
      $_SESSION['afficherSmenu1'] = '';
      $_SESSION['afficherSmenu2'] = 'hidden';
      $_SESSION['afficherSmenu3'] = '';
      $_SESSION['afficherSmenu4a'] = 'hidden';
      $_SESSION['afficherSmenu4b'] = '';
      $_SESSION['afficherSmenu5'] = '';
      $_SESSION['afficherSmenu6'] = '';
      include("vues/v_sommaire.php");
      echo 'visiteur'; // Message à la suite du menu 'Mon espace ...'
      break;
   }
   case("Délégué"):{
      $_SESSION['afficherSmenu1'] = '';
      $_SESSION['afficherSmenu2'] = '';
      $_SESSION['afficherSmenu3'] = '';
      $_SESSION['afficherSmenu4a'] = '';
      $_SESSION['afficherSmenu4b'] = '';
      $_SESSION['afficherSmenu5'] = '';
      $_SESSION['afficherSmenu6'] = '';
      include("vues/v_sommaire.php");
      echo 'délégué';
      echo '<ul class="subMenu ' . $_SESSION['activerSmenu7'] . '">
      <a href="index.php?page=7&uc=statistiques" title="Stastistiques de ma région">Stats région</a>
      </ul>';
      break;
   }
   case("Responsable"):{
      $_SESSION['afficherSmenu1'] = 'hidden';
      $_SESSION['afficherSmenu2'] = '';
      $_SESSION['afficherSmenu3'] = '';
      $_SESSION['afficherSmenu4a'] = '';
      $_SESSION['afficherSmenu4b'] = '';
      $_SESSION['afficherSmenu5'] = '';
      $_SESSION['afficherSmenu6'] = '';
      include("vues/v_sommaire.php");
      echo 'responsable';
      echo '<ul class="subMenu ' . $_SESSION['activerSmenu7'] . '">
      <a href="index.php?page=7&uc=statistiques" title="Statistiques de mon secteur">Stats secteur</a>
      </ul>';
      break;
   }
   default:{
      $_SESSION['afficherSmenu1'] = '';
      $_SESSION['afficherSmenu2'] = 'hidden';
      $_SESSION['afficherSmenu3'] = '';
      $_SESSION['afficherSmenu4a'] = 'hidden';
      $_SESSION['afficherSmenu4b'] = '';
      $_SESSION['afficherSmenu5'] = '';
      $_SESSION['afficherSmenu6'] = '';
      echo 'visiteur'; // aucun autre sous-menu attendu pour les visiteurs (valeur par défaut)
      break;
   }
}

// Fin du sommaire (suite de la dernière balise <li>)
echo '<ul class="subMenu">
         <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
      </ul>
   </li>
</div>';

?>