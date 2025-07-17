create table jogadores(

    id serial primary key,
    nome varchar(100) not null,
    data_nascimento date not null,
    altura int not null,
    peso float not null,
    posicao varchar(100) not null

);