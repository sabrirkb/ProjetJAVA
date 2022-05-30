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
            throw new Exception("Erreur &agrave; la connexion \n" . $e->getMessage());
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
    public function getPraticiens() {
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
        $req="select MED_DEPOTLEGAL, MED_NOMCOMMERCIAL from medicament order by MED_NOMCOMMERCIAL";
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


	// public function AjouterCR($Matricule, $Numero, $praticien, $dateVisite, $bilan, $Motif)
	// {
	// 	$req="insert into rapport_visite(VIS_MATRICULE, RAP_NUM, PRA_NUM, RAP_DATE, RAP_BILAN, RAP_MOTIF) 
	// 	VALUES (:pVIS_MATRICULE, :pRAP_NUM, :pPRA_NUM, :pRAP_DATE, :pRAP_BILAN, :pRAP_MOTIF)";
    //     try {
	// 		$prep = PdoGsb::$monPdo->prepare($req);
	// 		$prep->bindValue(':pVIS_MATRICULE', $Matricule,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_NUM', $Numero,PDO::PARAM_INT);
	// 		$prep->bindValue(':pPRA_NUM', $praticien,PDO::PARAM_INT);
	// 		$prep->bindValue(':pRAP_DATE', $dateVisite,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_BILAN', $bilan,PDO::PARAM_STR);
	// 		$prep->bindValue(':pRAP_MOTIF', $Motif,PDO::PARAM_STR);
	// 		$prep->execute();
	// 		$result=$prep->fetch(PDO::FETCH_ASSOC);
	// 		return $result;
    //     }

    //     catch (Exception $e) 
    //     {
    //         echo 'Exception reçue : ',  $e->getMessage(), "\n";
    //     }
	// }

	public function AjouterCR($Matricule,$Numero,$praticien,$dateVisite,$bilan,$Motif){ 
		$prep = PdoGsb::$monPdo->prepare("INSERT INTO rapport_visite VALUES (:pVIS_MATRICULE, :pRAP_NUM, :pPRA_NUM, :pRAP_DATE, :pRAP_BILAN, :pRAP_MOTIF)");
		try{
			$prep->bindValue(':pVIS_MATRICULE', $Matricule, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_NUM', $Numero, PDO::PARAM_INT);
			$prep->bindValue(':pPRA_NUM', $praticien, PDO::PARAM_INT);
			$prep->bindValue(':pRAP_DATE', $dateVisite, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_BILAN', $bilan, PDO::PARAM_STR);
			$prep->bindValue(':pRAP_MOTIF', $Motif, PDO::PARAM_STR);
			$success = $prep->execute();
		
			if($success){
				return true;
			}else{
				return false;
			}
		}
		catch(Exception $e){
			echo 'Exception reçue : ',  $e->getMessage(), "\n";
		}
	}

	public function getLesMedicaments($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `medicament` LIMIT :value, 1";
		try 
			{
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getLesPraticiens($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `praticien` ORDER BY PRA_NOM LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getLesActivites($value) {
		// retroune le médicament choisi avec les boutons de navigation [« Prec][$value][Suiv »]
		$req = "select * FROM `activite_compl` LIMIT :value, 1";
		try 
			{
				$value -= 1;
				$prep = PdoGsb::$monPdo->prepare($req);
				$prep->bindValue(':value', $value,PDO::PARAM_INT);
				$prep->execute();
				$result=$prep->fetchAll(PDO::FETCH_ASSOC);
				return $result;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxMedicaments() {
		// retroune le nombre max de médicaments dans la database
		$req = "SELECT COUNT(*) AS MaxMed FROM medicament;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxMed']-1;
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxPraticiens() {
		// retroune le nombre max de praticiens dans la database
		$req = "SELECT COUNT(*) AS MaxPrat FROM praticien;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxPrat'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

	public function getMaxActivites() {
		// retroune le nombre max de praticiens dans la database
		$req = "SELECT COUNT(*) AS MaxActiv FROM activite_compl;";
		try 
			{
				$query = PdoGsb::$monPdo->query($req);
				$result = $query->fetch(PDO::FETCH_ASSOC);
				return $result['MaxActiv'];
			}
		catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
	}

    public function retournerLeDernierNumRap()
{
    // renvoie un result avec la liste des raports
    $req = "SELECT RAP_NUM
            FROM rapport_visite
            ORDER BY RAP_NUM DESC
            LIMIT 1";
			try
			{
				$query =PdoGsb::$monPdo -> query($req);
		        $result = $query -> fetch(PDO::FETCH_ASSOC);
		        return $result['RAP_NUM'];
			}
			catch (Exception $e)
		{
			echo 'Exception reçue : ', $e->getMessage(), "\n";
		}
    
}
}
  ?>