<?php


include("controleurs/c_Sommaire.php");

if(!isset($_REQUEST['action'])){
	$_REQUEST['action'] = 'default';
}

$action = $_REQUEST['action'];

if(isset($_SESSION['AC_index']))
{
    $index = $_SESSION['AC_index'];
}
else
{
    $index = 1;
}                            
switch($action){
    case 'suivant':
        {
            $index = $index + 1;
            if ($index <= $pdo->getMaxActivites())
            {
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            else
            {
                $index = $pdo->getMaxActivites();
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            $_SESSION['AC_index'] = $index;
            
            include("vues/v_activitesComplementaires.php");
            break;
        }
    case 'precedent':
        {
            $index = $index - 1;
            if ($index >= 1)
            {
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            else
            {
                $index = 1;
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            $_SESSION['AC_index'] = $index;
            
            include("vues/v_activitesComplementaires.php");
            break;
        }
    case 'custom': 
        {
            $index = $_POST['updateIndex'];
            if ($index >= 1 and $index <= $pdo->getMaxActivites())
            {
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            if ($index < 1)
            {
                $index = 1;
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            if ($index > $pdo->getMaxActivites())
            {
                $index = $pdo->getMaxActivites();
                $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }
            }
            $_SESSION['AC_index'] = $index;
            
            include("vues/v_activitesComplementaires.php");
            break;
        }

        case 'saisie': 
            {
                include("vues/v_saisieActivite.php");
                break;
            }
            case 'validerSaisieActivite':
                {
                    $message = "Activité complémentaire ajoutée avec succès.";
                    include("vues/v_Message.php"); 
                    break; 
                }
        

    default: 
        {
            $_SESSION['AC_index'] = 1;
            $index = 1;
            $lesActivites = $pdo->getLesActivites($index);
                foreach($lesActivites as $uneActivite)
                {
                    $_SESSION['AC_num'] = $uneActivite['AC_NUM'];
                    $_SESSION['AC_date'] = $uneActivite['AC_DATE'];
                    $_SESSION['AC_lieu'] = $uneActivite['AC_LIEU'];
                    $_SESSION['AC_theme'] = $uneActivite['AC_THEME'];
                    $_SESSION['AC_motif'] = $uneActivite['AC_MOTIF'];
                }

                
            include("vues/v_activitesComplementaires.php");
            break;
        }
    }


?>
