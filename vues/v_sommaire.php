    <!-- Division pour le sommaire -->
    <div id="menuGauche">
     <div id="infosUtil">
    
         <h2>
            Bienvenue,<br>
            <strong>
               <?php echo $_SESSION['prenom'];?> <?php echo $_SESSION['nom'];?>
            </strong>
         </h2>
          </div>  
        <ul id="menuList">
            <li class="smenu">
                   Comptes rendus 
                   <ul class="subMenu <?php echo $_SESSION['activerSmenu1']; ?>" <?php echo $_SESSION['afficherSmenu1']; ?>>
              <a href="index.php?page=1&uc=gererCR&action=saisirCR" title="Nouveaux comptes rendus">Nouveau</a>
			  
           </ul>         
		   <ul class="subMenu <?php echo $_SESSION['activerSmenu2']; ?>" <?php echo $_SESSION['afficherSmenu2']; ?>>
              <a href="index.php?page=2&uc=gererCR&action=consulterCR" title="Consulter les comptes rendus">Consulter</a>
			  </ul>
           </li>
		 
           <li class="smenu">Médicaments
                         <ul class="subMenu <?php echo $_SESSION['activerSmenu3']; ?>" <?php echo $_SESSION['afficherSmenu3']; ?>>
                            <a href="index.php?page=3&uc=medicaments&action=default" title="Les médicaments">Consulter</a>
                         </ul>
                         </li>             
           <li class="smenu">Praticiens
                            <ul class="subMenu <?php echo $_SESSION['activerSmenu4a']; ?>" <?php echo $_SESSION['afficherSmenu4a']; ?>>
                                  <a href="index.php?page=4a&uc=praticien&action=saisie" title="Les praticiens">Nouveau</a>
                                  </ul>
                              <ul class="subMenu <?php echo $_SESSION['activerSmenu4b']; ?>" <?php echo $_SESSION['afficherSmenu4b']; ?>>
                                  <a href="index.php?page=4b&uc=praticien&action=default" title="Les praticiens">Consulter</a>
                                  </ul>
                               </li>
           <li class="smenu">Activités complémentaires
                            <ul class="subMenu <?php echo $_SESSION['activerSmenu5']; ?>" <?php echo $_SESSION['afficherSmenu5']; ?>>
                                  <a href="index.php?page=5&uc=ac&action=saisie" title="Les activités complémentaires">Nouveau</a>
                                  </ul>
                              <ul class="subMenu <?php echo $_SESSION['activerSmenu6']; ?>" <?php echo $_SESSION['afficherSmenu6']; ?>>
                                  <a href="index.php?page=6&uc=ac&action=default" title="Les activités complémentaires">Consulter</a>
                                  </ul>
                               </li>
           <li class="smenu">Mon espace

		   
    