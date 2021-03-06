--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.4
-- Dumped by pg_dump version 9.1.4
-- Started on 2012-08-17 04:51:44 IST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 169 (class 3079 OID 11681)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1937 (class 0 OID 0)
-- Dependencies: 169
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 168 (class 1259 OID 24605)
-- Dependencies: 5
-- Name: favorites; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE favorites (
    tweet_id integer,
    user_id integer,
    creator_id integer
);


ALTER TABLE public.favorites OWNER TO postgres;

--
-- TOC entry 162 (class 1259 OID 24579)
-- Dependencies: 5
-- Name: feeds; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE feeds (
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


ALTER TABLE public.feeds OWNER TO postgres;

--
-- TOC entry 161 (class 1259 OID 24577)
-- Dependencies: 5 162
-- Name: feeds_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE feeds_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.feeds_id_seq OWNER TO postgres;

--
-- TOC entry 1938 (class 0 OID 0)
-- Dependencies: 161
-- Name: feeds_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE feeds_id_seq OWNED BY feeds.id;


--
-- TOC entry 1939 (class 0 OID 0)
-- Dependencies: 161
-- Name: feeds_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('feeds_id_seq', 1, true);


--
-- TOC entry 163 (class 1259 OID 24585)
-- Dependencies: 5
-- Name: followers; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE followers (
    user_id integer,
    follower_id integer
);


ALTER TABLE public.followers OWNER TO postgres;

--
-- TOC entry 164 (class 1259 OID 24588)
-- Dependencies: 5
-- Name: following; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE following (
    user_id integer,
    following_id integer
);


ALTER TABLE public.following OWNER TO postgres;

--
-- TOC entry 167 (class 1259 OID 24602)
-- Dependencies: 5
-- Name: retweets; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE retweets (
    tweet_id integer,
    user_id integer,
    creator_id integer
);


ALTER TABLE public.retweets OWNER TO postgres;

--
-- TOC entry 166 (class 1259 OID 24593)
-- Dependencies: 5
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    id integer NOT NULL,
    email character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 24591)
-- Dependencies: 5 166
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 1940 (class 0 OID 0)
-- Dependencies: 165
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1941 (class 0 OID 0)
-- Dependencies: 165
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 1, true);


--
-- TOC entry 1912 (class 2604 OID 24582)
-- Dependencies: 162 161 162
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feeds ALTER COLUMN id SET DEFAULT nextval('feeds_id_seq'::regclass);


--
-- TOC entry 1931 (class 0 OID 24605)
-- Dependencies: 168
-- Data for Name: favorites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY favorites (tweet_id, user_id, creator_id) FROM stdin;
\.


--
-- TOC entry 1926 (class 0 OID 24579)
-- Dependencies: 162
-- Data for Name: feeds; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY feeds (tweet, user_id, id, "timestamp", receiver_id, tweet_id, creator_id, creator_name, creator_email, username) FROM stdin;
\.


--
-- TOC entry 1927 (class 0 OID 24585)
-- Dependencies: 163
-- Data for Name: followers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY followers (user_id, follower_id) FROM stdin;
\.


--
-- TOC entry 1928 (class 0 OID 24588)
-- Dependencies: 164
-- Data for Name: following; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY following (user_id, following_id) FROM stdin;
\.


--
-- TOC entry 1930 (class 0 OID 24602)
-- Dependencies: 167
-- Data for Name: retweets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY retweets (tweet_id, user_id, creator_id) FROM stdin;
\.


--
-- TOC entry 1929 (class 0 OID 24593)
-- Dependencies: 166
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (username, password, id, email) FROM stdin;
\.


--
-- TOC entry 1916 (class 2606 OID 24584)
-- Dependencies: 162 162
-- Name: feeds_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY feeds
    ADD CONSTRAINT feeds_pkey PRIMARY KEY (id);


--
-- TOC entry 1922 (class 2606 OID 24601)
-- Dependencies: 166 166
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 1925 (class 1259 OID 32831)
-- Dependencies: 168
-- Name: favorites_creator_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX favorites_creator_id_idx ON favorites USING btree (creator_id DESC);


--
-- TOC entry 1913 (class 1259 OID 32834)
-- Dependencies: 162 162
-- Name: feeds_creator_id_tweet_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX feeds_creator_id_tweet_id_idx ON feeds USING btree (creator_id DESC, tweet_id DESC);


--
-- TOC entry 1914 (class 1259 OID 32833)
-- Dependencies: 162
-- Name: feeds_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX feeds_id_idx ON feeds USING btree (id DESC);


--
-- TOC entry 1917 (class 1259 OID 32832)
-- Dependencies: 162
-- Name: feeds_user_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX feeds_user_id_idx ON feeds USING btree (user_id DESC);


--
-- TOC entry 1918 (class 1259 OID 32830)
-- Dependencies: 163
-- Name: followers_user_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX followers_user_id_idx ON followers USING btree (user_id DESC);


--
-- TOC entry 1919 (class 1259 OID 32829)
-- Dependencies: 164
-- Name: following_user_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX following_user_id_idx ON following USING btree (user_id DESC);


--
-- TOC entry 1924 (class 1259 OID 32828)
-- Dependencies: 167
-- Name: retweets_creator_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX retweets_creator_id_idx ON retweets USING btree (creator_id DESC);


--
-- TOC entry 1920 (class 1259 OID 32826)
-- Dependencies: 166
-- Name: users_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX users_id_idx ON users USING btree (id);


--
-- TOC entry 1923 (class 1259 OID 32825)
-- Dependencies: 166
-- Name: users_username_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX users_username_idx ON users USING btree (username DESC NULLS LAST);


--
-- TOC entry 1936 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2012-08-17 04:51:47 IST

--
-- PostgreSQL database dump complete
--

