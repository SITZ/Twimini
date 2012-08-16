PGDMP         .                p            minitwittershard    9.1.4    9.1.4     p           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            q           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            r           1262    32815    minitwittershard    DATABASE     v   CREATE DATABASE minitwittershard WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_IN' LC_CTYPE = 'en_IN';
     DROP DATABASE minitwittershard;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            s           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    5            t           0    0    public    ACL     �   REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
                  postgres    false    5            �            3079    11681    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            u           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    164            �            1259    32816    auth    TABLE     y   CREATE TABLE auth (
    user_id integer,
    user_name character varying,
    access_token character varying NOT NULL
);
    DROP TABLE public.auth;
       public         postgres    false    5            �            1259    32849    user_id_seq    SEQUENCE     m   CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.user_id_seq;
       public       postgres    false    5            v           0    0    user_id_seq    SEQUENCE SET     2   SELECT pg_catalog.setval('user_id_seq', 7, true);
            public       postgres    false    163            �            1259    32837    shards    TABLE     �   CREATE TABLE shards (
    userid integer DEFAULT nextval('user_id_seq'::regclass) NOT NULL,
    username character varying,
    shard integer,
    email character varying
);
    DROP TABLE public.shards;
       public         postgres    false    1894    5            n          0    32816    auth 
   TABLE DATA               9   COPY auth (user_id, user_name, access_token) FROM stdin;
    public       postgres    false    161   R       o          0    32837    shards 
   TABLE DATA               9   COPY shards (userid, username, shard, email) FROM stdin;
    public       postgres    false    162   �       h           2606    32823 	   auth_pkey 
   CONSTRAINT     O   ALTER TABLE ONLY auth
    ADD CONSTRAINT auth_pkey PRIMARY KEY (access_token);
 8   ALTER TABLE ONLY public.auth DROP CONSTRAINT auth_pkey;
       public         postgres    false    161    161            k           2606    32844    shards_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY shards
    ADD CONSTRAINT shards_pkey PRIMARY KEY (userid);
 <   ALTER TABLE ONLY public.shards DROP CONSTRAINT shards_pkey;
       public         postgres    false    162    162            i           1259    32848    shards_email_idx    INDEX     B   CREATE INDEX shards_email_idx ON shards USING btree (email DESC);
 $   DROP INDEX public.shards_email_idx;
       public         postgres    false    162            l           1259    32846    shards_userid_idx    INDEX     D   CREATE INDEX shards_userid_idx ON shards USING btree (userid DESC);
 %   DROP INDEX public.shards_userid_idx;
       public         postgres    false    162            m           1259    32847    shards_username_idx    INDEX     H   CREATE INDEX shards_username_idx ON shards USING btree (username DESC);
 '   DROP INDEX public.shards_username_idx;
       public         postgres    false    162            n   %   x�3�,�,I-��40�2�,.�,J��44����� y]/      o   8   x�3�,�,I-��4�2�R2�R�K2���s��9��2�s9����D1z\\\ @T�     