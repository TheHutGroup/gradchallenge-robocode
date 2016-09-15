use robocode;

create table player (playerid int, author varchar(30), version varchar(10), jarFile varchar(30), fullClassName varchar(50));

create table roundinfo (battleid int, roundid int, playerid int, energyLeft int, ramDamage int, gunDamage int);

create table battleinfo (battleid int, playerid int, rank int);

