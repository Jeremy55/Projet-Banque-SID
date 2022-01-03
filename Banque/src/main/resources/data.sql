

INSERT INTO compte (id,IBAN,solde) VALUES
(1,'FR761234567890123456789012345','100');

INSERT INTO carte (id,numero,code,cryptogramme,active,contact,virtuelle,localisation,plafond,expiration,compte_id) VALUES
    (1,'1234567890123456','3678','1234',1,1,0,0,1000,'2022-01-30',1);

INSERT INTO operation (id,libelle,montant,montant_apres_conversion,taux_conversion,devise,categorie,pays,date,compte_id) VALUES
    (1,'Intermarché-Commercy','100','100','1','EUR','achat','France','2022-01-01','1');

INSERT INTO client (id,nom,prenom,pays,no_passeport,telephone,secret,compte_id) VALUES
(1,'Dupont','Jean','France','123456789','0123456789','123456789',1);


