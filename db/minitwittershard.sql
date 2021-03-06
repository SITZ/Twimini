--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.4
-- Dumped by pg_dump version 9.1.4
-- Started on 2012-08-17 04:51:55 IST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 164 (class 3079 OID 11681)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1909 (class 0 OID 0)
-- Dependencies: 164
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 161 (class 1259 OID 32816)
-- Dependencies: 5
-- Name: auth; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE auth (
    user_id integer,
    user_name character varying,
    access_token character varying NOT NULL
);


ALTER TABLE public.auth OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 32849)
-- Dependencies: 5
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO postgres;

--
-- TOC entry 1910 (class 0 OID 0)
-- Dependencies: 163
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('user_id_seq', 1, true);


--
-- TOC entry 162 (class 1259 OID 32837)
-- Dependencies: 1894 5
-- Name: shards; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE shards (
    userid integer DEFAULT nextval('user_id_seq'::regclass) NOT NULL,
    username character varying,
    shard integer,
    email character varying
);


ALTER TABLE public.shards OWNER TO postgres;

--
-- TOC entry 1902 (class 0 OID 32816)
-- Dependencies: 161
-- Data for Name: auth; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY auth (user_id, user_name, access_token) FROM stdin;
\.


--
-- TOC entry 1903 (class 0 OID 32837)
-- Dependencies: 162
-- Data for Name: shards; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY shards (userid, username, shard, email) FROM stdin;
\.


--
-- TOC entry 1896 (class 2606 OID 32823)
-- Dependencies: 161 161
-- Name: auth_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY auth
    ADD CONSTRAINT auth_pkey PRIMARY KEY (access_token);


--
-- TOC entry 1899 (class 2606 OID 32844)
-- Dependencies: 162 162
-- Name: shards_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY shards
    ADD CONSTRAINT shards_pkey PRIMARY KEY (userid);


--
-- TOC entry 1897 (class 1259 OID 32848)
-- Dependencies: 162
-- Name: shards_email_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX shards_email_idx ON shards USING btree (email DESC);


--
-- TOC entry 1900 (class 1259 OID 32846)
-- Dependencies: 162
-- Name: shards_userid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX shards_userid_idx ON shards USING btree (userid DESC);


--
-- TOC entry 1901 (class 1259 OID 32847)
-- Dependencies: 162
-- Name: shards_username_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX shards_username_idx ON shards USING btree (username DESC);


--
-- TOC entry 1908 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2012-08-17 04:51:57 IST

--
-- PostgreSQL database dump complete
--

