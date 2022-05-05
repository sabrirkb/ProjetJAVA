<form action="index.php?do=lesEntreprises&action=validerAjouterEntreprise" method="post">
<div id="contenu">

<!-- Création d'un formulaire permettant l'ajout d'une entreprise dans la base de données -->

      <h2>Ajouter une entreprise</h2>

      <table class = "ajouter">


      <!-- Création d'une zone de texte "Raison sociale" -->
      <tr> <td class = "ajouter"> Raison sociale : * </td>
      <td class = "ajouter"> <input size="22" type="text" name="raisonsociale" required> </td> </tr>


      <!-- Création d'une zone de texte "Adresse" -->
      <tr> <td class = "ajouter"> Adresse : * </td>
      <td class = "ajouter"><input size="22" type="text" name="adresse" required>  </td> </tr>


      <!-- Création d'une zone de texte "Code postal" -->
      <tr> <td class = "ajouter"> Code postal : * </td>
      <td class = "ajouter"><input size="22" type="text" name="codepostal" pattern="[0-9]{5}" required>  </td></tr>


      <!-- Création d'une zone de texte "Ville" -->
      <tr> <td class = "ajouter"> Ville : * </td>
      <td class = "ajouter"><input size="22" type="text" name="ville" required>  </td> </tr>


      <!-- Création d'une zone de texte "Téléphone" -->
      <tr> <td class = "ajouter"> Téléphone : * </td>
      <td class = "ajouter"><input size="22" type="text" name="telephone" pattern="^(0)[1-9]([-. ]?[0-9]{2}){4}$" required>  </td> 
      </tr>


      <!-- Création d'une zone de texte "Fax" non obligatoire -->
      <tr> <td class = "ajouter"> Fax : </td>
      <td class = "ajouter"><input size="22" type="text" name="fax" pattern="^(0)[1-9]([-. ]?[0-9]{2}){4}$">  </td> </tr>


      <!-- Création d'une zone de texte "Mail" -->
      <tr> <td class = "ajouter"> Mail : * </td>
      <td class = "ajouter"><input size="27" type="email" name = "mail" pattern="[^@]+@[^@]+\.[a-zA-Z]{2,}" required> </td> </tr>


      <!-- Création d'une liste déroulante avec une zone de texte possédant l'id du statut d'entreprise en "value", le libellé 
      donne des détails sur le statut -->
      <label for ="choix_statuts"> <tr> <td class = "ajouter"> Statut : * </td><td class = "ajouter"></label>
      <input list="statuts" size="22" type="text" name="choix_statut" required autocomplete = 'off'>
      <datalist id="statuts">
      <?php  foreach ($lesStatuts as $unStatut) {
            $id = $unStatut['idstatut'];
            $libelle = $unStatut['libellestatut'];

      echo'<option value='.$id.'>'.$libelle.'</option>';
}

?>
      </datalist></select> </td>
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