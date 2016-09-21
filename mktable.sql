use robocode;

create table player (playerid varchar(30), author varchar(30), version varchar(10), jarFile varchar(30), fullClassName varchar(50));

create table roundinfo (battleid int, roundid int, playerid varchar(30), energyLeft int, ramDamage int, gunDamage int);

create table battleinfo (battleid int, playerid varchar(30), rank int);

