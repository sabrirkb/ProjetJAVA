<form action="index.php?uc=CompteRendu&action=validerSaisieCR" method="post">
<div id="contenu">

<!-- Création d'un formulaire permettant l'ajout d'un CR dans la base de données -->

      <h2>Rapport de visite</h2>

      <table class = "ajouter">


      <!-- Création d'une zone de texte "numero" -->
      <tr> <td class = "ajouter"> Numero : * </td>
      <td class = "ajouter"> <input size="22" type="text" name="numero" required> </td> </tr>


      <!-- Création d'une zone de texte "Date visite" -->
      <tr> <td class = "ajouter"> Date Visite : * </td>
      <td class = "ajouter"><input size="22" type="text" name="dateVisite" required>  </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du praticien en "value", le nom et le prénom
du praticien donnent des détails sur le praticien-->
<label for ="choix_praticien"> Praticien : </label>
      <input list="praticiens" type="text" name="choix_praticien" required autocomplete = 'off'>
      <datalist id="praticiens">
      <?php  foreach ($lesEPraticiens as $unPraticien) {
                         $idPraticien = $unPraticien['pra_num'];
                         $idPraticien = $unPraticien['pra_nom'];
                         $idPraticien = $unPraticien['pra_prenom'];
        
      echo'<option value='.$idPraticien.'>'.$nomPraticien.' '.$prenomPraticien.'</option>';
}
?>
</datalist></select> </td>


      <!-- Création d'une zone de texte "coefficient" -->
      <tr> <td class = "ajouter"> coefficient: * </td>
      <td class = "ajouter"><input size="22" type="text" name="ville" required>  </td> </tr>


      <!-- Création d'une case à cocher "remplacant" -->
      <tr> <td class = "ajouter"> Remplaçant : * </td>
      <td class = "ajouter"><input size="22" type="checkbox" name="remplacant" required>  </td> 
      </tr>


      <!-- Création d'une zone de texte "date" non obligatoire -->
      <tr> <td class = "ajouter"> Date de visite : * </td>
      <td class = "ajouter"><input size = "22" type="date" name="dateVisite" required> </td> </tr>


      <!-- Création d'une liste déroulante "motif" (a voir apres)-->
     


      <!-- Création d'une zone de texte "bilan" -->
      <tr> <td class = "ajouter"> Bilan: * </td>
      <td class = "ajouter"><input size="22" type="text" name="bilan" required>  </td> </tr>


?>
      
      <td>
      <input type="button" onclick="window.location.replace('http://localhost/application/GestionDesStagesV0/index.php?do=lesEntreprises&action=statut')" value="Ajouter un statut d'entreprise" /> </td>
      </tr></table> 
      <p>

<!-- Aide pour l'utilisateur signifiant que les champs précédés d'une astérisque sont obligatoires -->
      * : Obligatoire
      <p>
      <input type="submit" name="Valider">       
      </div>
</form>