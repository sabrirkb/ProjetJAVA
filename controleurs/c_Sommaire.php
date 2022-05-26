<?php

/*Ce controleur permet de colorer la page de navigation active dans le sommaire*/

include("vues/v_sommaire.php");

if(!isset($_REQUEST['page']))
{
	$_REQUEST['page'] = 'default';
}
$page = $_REQUEST['page'];
switch($page)
{
    case("1"):
        {
            echo
            '<ul class="subMenu active">
                  <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
                  
               </ul>         
               <ul class="subMenu">
                  <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                  </ul>
               </li>
             
               <li class="smenu">
                  <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
               </li>             
               </li><li class="smenu">
                  <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
               </li>
              <li class="smenu">
                  <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
               </li>
               <li class="smenu">
                  <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
               </li>
             </ul>
            </div>'
            ;
            break;
        }
        case("2"):
        {
            echo
            '<ul class="subMenu">
                  <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
                  
               </ul>         
               <ul class="subMenu active">
                  <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                  </ul>
               </li>
             
               <li class="smenu">
                  <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
               </li>             
               </li><li class="smenu">
                  <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
               </li>
              <li class="smenu">
                  <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
               </li>
               <li class="smenu">
                  <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
               </li>
             </ul>
            </div>'
            ;
            break;
        }
        case("3"):
            {
                echo
                '<ul class="subMenu">
                      <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
                      
                   </ul>         
                   <ul class="subMenu">
                      <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                      </ul>
                   </li>
                 
                   <li class="smenu active">
                      <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
                   </li>             
                   </li><li class="smenu">
                      <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
                   </li>
                  <li class="smenu">
                      <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
                   </li>
                   <li class="smenu">
                      <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                   </li>
                 </ul>
                </div>'
                ;
                break;
            }
            case("4"):
                {
                    echo
                    '<ul class="subMenu">
                          <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
                          
                       </ul>         
                       <ul class="subMenu">
                          <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                          </ul>
                       </li>
                     
                       <li class="smenu">
                          <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
                       </li>             
                       </li><li class="smenu active">
                          <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
                       </li>
                      <li class="smenu">
                          <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
                       </li>
                       <li class="smenu">
                          <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                       </li>
                     </ul>
                    </div>'
                    ;
                    break;
                }
                case("5"):
                    {
                        echo
                        '<ul class="subMenu">
                              <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
                              
                           </ul>         
                           <ul class="subMenu">
                              <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                              </ul>
                           </li>
                         
                           <li class="smenu">
                              <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
                           </li>             
                           </li><li class="smenu">
                              <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
                           </li>
                          <li class="smenu active">
                              <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
                           </li>
                           <li class="smenu">
                              <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                           </li>
                         </ul>
                        </div>'
                        ;
                        break;
                    }
    default:
    {
        echo
        '<ul class="subMenu">
              <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveaux</a>
			  
           </ul>         
		   <ul class="subMenu">
              <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
			  </ul>
           </li>
		 
           <li class="smenu">
              <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Médicaments</a>
           </li>             
           </li><li class="smenu">
              <a href="index.php?page=4&uc=praticiens&action=default" title="Les praticiens">Praticiens</a>
           </li>
		  <li class="smenu">
              <a href="index.php?page=5&uc=activites" title="Les activités complémentaires">Activités complémentaires</a>
           </li>
           <li class="smenu">
              <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
           </li>
         </ul>
        </div>'
        ;
        break;
    }
}

?>