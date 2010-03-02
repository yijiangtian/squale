--###########################################################
-- Modifications for the shared repository : import reference

create table Tag_Segementation (
    segmentationId bigint not null,
    TagId bigint not null,
    primary key (segmentationId, TagId)
);
    
create table segmentation (
    segmentationId bigint generated by default as identity (start with 1),
    primary key (segmentationId)
);

create table shared_repo_stats (
    StatsId bigint generated by default as identity (start with 1),
    elementType varchar(255),
    dataType varchar(255),
    dataName varchar(255),
    language varchar(255),
    mean float,
    max float,
    min float,
    deviation float,
    elements float,
    segmentationId bigint not null,
    primary key (StatsId)
);

create table squale_params (
    SqualeParamsId bigint generated by default as identity (start with 1),
    paramKey varchar(255) not null,
    paramaValue varchar(255) not null,
    primary key (SqualeParamsId)
);
    
alter table Tag_Segementation 
    add constraint FK328D3382BBF32679 
    foreign key (segmentationId) 
    references segmentation
    on delete cascade;

alter table Tag_Segementation 
    add constraint FK328D3382FD9106F6 
    foreign key (TagId) 
    references Tag
    on delete cascade;

alter table shared_repo_stats 
    add constraint FK2F1F0DACBBF32679 
    foreign key (segmentationId) 
    references segmentation
    on delete cascade;
    
--###########################################################