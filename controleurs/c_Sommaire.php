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
                  <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                  
               </ul>         
               <ul class="subMenu">
                  <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                  </ul>
               </li>
             
               <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
               <li class="smenu">Praticiens
               <ul class="subMenu">
                     <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                     </ul>
                 <ul class="subMenu">
                     <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                     </ul>
                  </li>
               <li class="smenu">Activités complémentaires
               <ul class="subMenu">
                     <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                     </ul>
                 <ul class="subMenu">
                     <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                     </ul>
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
                  <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                  
               </ul>         
               <ul class="subMenu active">
                  <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                  </ul>
               </li>
             
               <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
               <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
               <li class="smenu">Activités complémentaires
               <ul class="subMenu">
                     <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                     </ul>
                 <ul class="subMenu">
                     <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                     </ul>
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
                      <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                      
                   </ul>         
                   <ul class="subMenu">
                      <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                      </ul>
                   </li>
                 
                   <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
                   <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
                   <li class="smenu">Activités complémentaires
                   <ul class="subMenu">
                         <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                         </ul>
                     <ul class="subMenu">
                         <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                         </ul>
                      </li>
                   <li class="smenu">
                      <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                   </li>
                 </ul>
                </div>'
                ;
                break;
            }
            case("4a"):
                {
                    echo
                    '<ul class="subMenu">
                          <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                          
                       </ul>         
                       <ul class="subMenu">
                          <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                          </ul>
                       </li>
                     
                       <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
                       <li class="smenu">Praticiens
                            <ul class="subMenu active">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
                       <li id="smenu">Activités complémentaires
                       <ul class="subMenu">
                             <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                             </ul>
                         <ul class="subMenu">
                             <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                             </ul>
                          </li>
                       <li class="smenu">
                          <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                       </li>
                     </ul>
                    </div>'
                    ;
                    break;
                }
                case("4b"):
                  {
                      echo
                      '<ul class="subMenu">
                            <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                            
                         </ul>         
                         <ul class="subMenu">
                            <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                            </ul>
                         </li>
                        <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>     
                         <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu active">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
                         <li id="smenu">Activités complémentaires
                         <ul class="subMenu">
                               <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                               </ul>
                           <ul class="subMenu">
                               <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                               </ul>
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
                              <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                              
                           </ul>         
                           <ul class="subMenu">
                              <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                              </ul>
                           </li>
                         
                           <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
                           <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
                           <li class="smenu">Activités complémentaires
                            <ul class="subMenu active">
                                  <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                                  </ul>
                               </li>
                           <li class="smenu">
                              <a href="index.php?uc=connexion&action=deconnexion" title="Se déconnecter">Déconnexion</a>
                           </li>
                         </ul>
                        </div>'
                        ;
                        break;
                    }
                    case("6"):
                     {
                         echo
                         '<ul class="subMenu">
                               <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
                               
                            </ul>         
                            <ul class="subMenu">
                               <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
                               </ul>
                            </li>
                          
                            <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>
                            <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
                            <li class="smenu">Activités complémentaires
                            <ul class="subMenu">
                                  <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                                  </ul>
                              <ul class="subMenu active">
                                  <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                                  </ul>
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
              <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
			  
           </ul>         
		   <ul class="subMenu">
              <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
			  </ul>
           </li>
		 
           <li class="smenu">Médicaments
                         <ul class="subMenu">
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>             
           <li class="smenu">Praticiens
                            <ul class="subMenu">
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
           <li class="smenu">Activités complémentaires
                            <ul class="subMenu">
                                  <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                                  </ul>
                              <ul class="subMenu">
                                  <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                                  </ul>
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