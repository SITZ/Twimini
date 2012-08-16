PGDMP         -                p            minitwitter1    9.1.4    9.1.4 &    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           1262    32835    minitwitter1    DATABASE     r   CREATE DATABASE minitwitter1 WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_IN' LC_CTYPE = 'en_IN';
    DROP DATABASE minitwitter1;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    5            �           0    0    public    ACL     �   REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
                  postgres    false    5            �            3079    11681    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    169            �            1259    24605 	   favorites    TABLE     ^   CREATE TABLE favorites (
    tweet_id integer,
    user_id integer,
    creator_id integer
);
    DROP TABLE public.favorites;
       public         postgres    false    5            �            1259    24579    feeds    TABLE     M  CREATE TABLE feeds (
    tweet character varying(140) NOT NULL,
    user_id integer,
    id integer NOT NULL,
    "timestamp" timestamp without time zone,
    receiver_id integer,
    tweet_id integer,
    creator_id integer,
    creator_name character varying,
    creator_email character varying,
    username character varying
);
    DROP TABLE public.feeds;
       public         postgres    false    5            �            1259    24577    feeds_id_seq    SEQUENCE     n   CREATE SEQUENCE feeds_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.feeds_id_seq;
       public       postgres    false    5    162            �           0    0    feeds_id_seq    SEQUENCE OWNED BY     /   ALTER SEQUENCE feeds_id_seq OWNED BY feeds.id;
            public       postgres    false    161            �           0    0    feeds_id_seq    SEQUENCE SET     5   SELECT pg_catalog.setval('feeds_id_seq', 640, true);
            public       postgres    false    161            �            1259    24585 	   followers    TABLE     I   CREATE TABLE followers (
    user_id integer,
    follower_id integer
);
    DROP TABLE public.followers;
       public         postgres    false    5            �            1259    24588 	   following    TABLE     J   CREATE TABLE following (
    user_id integer,
    following_id integer
);
    DROP TABLE public.following;
       public         postgres    false    5            �            1259    24602    retweets    TABLE     ]   CREATE TABLE retweets (
    tweet_id integer,
    user_id integer,
    creator_id integer
);
    DROP TABLE public.retweets;
       public         postgres    false    5            �            1259    24593    users    TABLE     �   CREATE TABLE users (
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    id integer NOT NULL,
    email character varying
);
    DROP TABLE public.users;
       public         postgres    false    5            �            1259    24591    users_id_seq    SEQUENCE     n   CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public       postgres    false    5    166            �           0    0    users_id_seq    SEQUENCE OWNED BY     /   ALTER SEQUENCE users_id_seq OWNED BY users.id;
            public       postgres    false    165            �           0    0    users_id_seq    SEQUENCE SET     4   SELECT pg_catalog.setval('users_id_seq', 41, true);
            public       postgres    false    165            x           2604    24582    id    DEFAULT     V   ALTER TABLE ONLY feeds ALTER COLUMN id SET DEFAULT nextval('feeds_id_seq'::regclass);
 7   ALTER TABLE public.feeds ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    162    161    162            �          0    24605 	   favorites 
   TABLE DATA               ;   COPY favorites (tweet_id, user_id, creator_id) FROM stdin;
    public       postgres    false    168   �#       �          0    24579    feeds 
   TABLE DATA               �   COPY feeds (tweet, user_id, id, "timestamp", receiver_id, tweet_id, creator_id, creator_name, creator_email, username) FROM stdin;
    public       postgres    false    162   	$       �          0    24585 	   followers 
   TABLE DATA               2   COPY followers (user_id, follower_id) FROM stdin;
    public       postgres    false    163   �$       �          0    24588 	   following 
   TABLE DATA               3   COPY following (user_id, following_id) FROM stdin;
    public       postgres    false    164   �$       �          0    24602    retweets 
   TABLE DATA               :   COPY retweets (tweet_id, user_id, creator_id) FROM stdin;
    public       postgres    false    167   
%       �          0    24593    users 
   TABLE DATA               7   COPY users (username, password, id, email) FROM stdin;
    public       postgres    false    166   '%       |           2606    24584 
   feeds_pkey 
   CONSTRAINT     G   ALTER TABLE ONLY feeds
    ADD CONSTRAINT feeds_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.feeds DROP CONSTRAINT feeds_pkey;
       public         postgres    false    162    162            �           2606    24601 
   users_pkey 
   CONSTRAINT     G   ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public         postgres    false    166    166            �           1259    32831    favorites_creator_id_idx    INDEX     R   CREATE INDEX favorites_creator_id_idx ON favorites USING btree (creator_id DESC);
 ,   DROP INDEX public.favorites_creator_id_idx;
       public         postgres    false    168            y           1259    32834    feeds_creator_id_tweet_id_idx    INDEX     b   CREATE INDEX feeds_creator_id_tweet_id_idx ON feeds USING btree (creator_id DESC, tweet_id DESC);
 1   DROP INDEX public.feeds_creator_id_tweet_id_idx;
       public         postgres    false    162    162            z           1259    32833    feeds_id_idx    INDEX     A   CREATE UNIQUE INDEX feeds_id_idx ON feeds USING btree (id DESC);
     DROP INDEX public.feeds_id_idx;
       public         postgres    false    162            }           1259    32832    feeds_user_id_idx    INDEX     D   CREATE INDEX feeds_user_id_idx ON feeds USING btree (user_id DESC);
 %   DROP INDEX public.feeds_user_id_idx;
       public         postgres    false    162            ~           1259    32830    followers_user_id_idx    INDEX     L   CREATE INDEX followers_user_id_idx ON followers USING btree (user_id DESC);
 )   DROP INDEX public.followers_user_id_idx;
       public         postgres    false    163                       1259    32829    following_user_id_idx    INDEX     L   CREATE INDEX following_user_id_idx ON following USING btree (user_id DESC);
 )   DROP INDEX public.following_user_id_idx;
       public         postgres    false    164            �           1259    32828    retweets_creator_id_idx    INDEX     P   CREATE INDEX retweets_creator_id_idx ON retweets USING btree (creator_id DESC);
 +   DROP INDEX public.retweets_creator_id_idx;
       public         postgres    false    167            �           1259    32826    users_id_idx    INDEX     <   CREATE UNIQUE INDEX users_id_idx ON users USING btree (id);
     DROP INDEX public.users_id_idx;
       public         postgres    false    166            �           1259    32825    users_username_idx    INDEX     X   CREATE UNIQUE INDEX users_username_idx ON users USING btree (username DESC NULLS LAST);
 &   DROP INDEX public.users_username_idx;
       public         postgres    false    166            �      x�3�4�4�2��f@2F��� #$�      �   �   x��ν
�0�9y�� ^������v�P��� ��F�ڥt�8\>�ɯK�x����"������ZH��rR�h�A�"_
�O�= u��8�^Ү/���Rx�3m��v�+�-�&)�8L{�u��KK�-�c}�ܳ,��<6`E+̛�?�g��G�:��he��i�f��R�@�g�      �      x�3�4����� �)      �      x�3�4����� �)      �      x������ � �      �   Z   x��A
� @ѵF�s��,oѮ�\D�ݟ�w���{��]d	����O�D^ ��0�=I��&���Lo�i��ڸ����������~���     