<?php
/** 
 * Classe d'accès aux données. 
  * Utilise les services de la classe PDO
 * pour l'application exempleMVC du cours sur la bdd bddemployés
 * Les attributs sont tous statiques,
 * les 4 premiers pour la connexion
 * $monPdo de type PDO 
 * $monPdo qui contiendra l'unique instance de la classe
 * @package default
 * @author AP modifié par ST
 * @version    1.0
 * @link       http://www.php.net/manual/fr/book.pdo.php
 */

class PdoGsb{   		
      	private static $serveur='mysql:host=localhost';
      	private static $bdd='dbname=gsb_visiteurs';   		
      	private static $user='sabrisokhna' ;    		
      	private static $mdp='PPPE-MVC!' ;
        private static $monPdo;
		private static $monPdoGsb=null;

/**
 * Constructeur privé, crée l'instance de PDO qui sera sollicitée
 * pour toutes les méthodes de la classe
 */				
	private function __construct(){
        try {
    	PdoGsb::$monPdo = new PDO(PdoGsb::$serveur.';'.PdoGsb::$bdd, PdoGsb::$user, PdoGsb::$mdp); 
		PdoGsb::$monPdo->query("SET CHARACTER SET utf8");
	    } catch (Exception $e) {
            throw new Exception("Erreur Ã  la connexion \n" . $e->getMessage());
        }
    }

	public function _destruct(){
		PdoGsb::$monPdo = null;
	}
/**
 * Fonction statique qui crée l'unique instance de la classe PdoExemple
 
 * Appel : $instancePdoExemple = PdoExemple::getPdoExemple();
 
 * @return l'unique objet de la classe PdoExemple
 */
	public  static function getPdoGsb(){
		if(PdoGsb::$monPdoGsb==null){
			PdoGsb::$monPdoGsb= new PdoGsb();
		}
		return PdoGsb::$monPdoGsb;  
	}
   public function getInfosVisiteur($login,$mdp){
       $req="select VIS_MATRICULE, VIS_NOM ,VIS_PRENOM from visiteur where LOGIN = :login and MDP = :mdp";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$prep->bindValue(':login', $login,PDO::PARAM_STR);
			$prep->bindValue(':mdp', $mdp,PDO::PARAM_STR);
			$prep->execute();
			$result=$prep->fetch(PDO::FETCH_ASSOC);
			return $result;
        }
        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }
   public function getLesVisiteurs() {
      // retourne un tableau associatif contenant tous les visiteurs
        $req="select vis_matricule, VIS_NOM, VIS_VILLE from visiteur";
		$rs = PdoGsb::$monPdo->query($req);
		$ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		return $ligne;
        // ou return $this->_pdo->query($query)->fetchAll(PDO::FETCH_ASSOC);
    }

	// Permet d'obtenir tous les praticiens triés par nom
    public function getLesPraticiens() {
        $req="select PRA_NUM, PRA_NOM, PRA_PRENOM from praticien order by PRA_NOM";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$rs = PdoGsb::$monPdo->query($req);
		    $ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		    return $ligne;
        }

        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }

   // Permet d'obtenir tous les médicaments
	public function getLesProduits() {
        $req="select MED_DEPOTLEGAL, MED_NOMCOMMERCIAL from medicament ";
        try {
			$prep = PdoGsb::$monPdo->prepare($req);
			$rs = PdoGsb::$monPdo->query($req);
		    $ligne = $rs->fetchAll(PDO::FETCH_ASSOC);
		    return $ligne;
        }

        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
    }




	public function AjouterCR($Matricule, $dateVisite, $praticien, $coef, $remplacant , $Echantillon, $Produit, $bilan, $documentation)
	{
		$req="insert into rapport_visite  VALUES (:pVIS_MATRICULE, :padresseent, :pcpent, :pvilleent, :ptelent, :pfaxent, :pcontactmailent, :pidstatut)";
        try {
        $prep = PdoExemple::$monPdo->prepare($req);
        $prep->bindValue(':praisonsocialeent', $raisonsociale,PDO::PARAM_STR);
        $prep->bindValue(':padresseent', $adresse,PDO::PARAM_STR);
        $prep->bindValue(':pcpent', $codepostal,PDO::PARAM_STR);
        $prep->bindValue(':pvilleent', $ville,PDO::PARAM_STR);
        $prep->bindValue(':ptelent', $tel,PDO::PARAM_STR);
        $prep->bindValue(':pfaxent', $fax,PDO::PARAM_STR);
        $prep->bindValue(':pcontactmailent', $mail,PDO::PARAM_STR);
        $prep->bindValue(':pidstatut', $idstatut,PDO::PARAM_INT);
        $prep->execute();
        }

        catch (Exception $e) 
        {
            echo 'Exception reçue : ',  $e->getMessage(), "\n";
        }
	}

	public function getLesMedicaments($value) {
		// retroune le médicament choisi avec les boutons de navigation [<] [>]
		$req = "SELECT * FROM `medicament` LIMIT :value , 1";
		try 
			{
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_STR);
				$prep->execute();
				$result=$prep->fetchAll();
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}
}   
  ?>