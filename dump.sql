--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: 458313
--

CREATE TABLE public.categories (
    id integer NOT NULL,
    category_name character varying(50) NOT NULL,
    weight character varying(5) NOT NULL
);


ALTER TABLE public.categories OWNER TO "458313";

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: 458313
--

CREATE SEQUENCE public.categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categories_id_seq OWNER TO "458313";

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: 458313
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- Name: class; Type: TABLE; Schema: public; Owner: 458313
--

CREATE TABLE public.class (
    class_id integer NOT NULL,
    course_number character varying(20) NOT NULL,
    term character varying(20) NOT NULL,
    section_number character varying(20) NOT NULL,
    description text
);


ALTER TABLE public.class OWNER TO "458313";

--
-- Name: class_class_id_seq; Type: SEQUENCE; Schema: public; Owner: 458313
--

CREATE SEQUENCE public.class_class_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.class_class_id_seq OWNER TO "458313";

--
-- Name: class_class_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: 458313
--

ALTER SEQUENCE public.class_class_id_seq OWNED BY public.class.class_id;


--
-- Name: items; Type: TABLE; Schema: public; Owner: 458313
--

CREATE TABLE public.items (
    id integer NOT NULL,
    itemname character varying(100) NOT NULL,
    category_name character varying(50),
    description text NOT NULL,
    point_value integer NOT NULL
);


ALTER TABLE public.items OWNER TO "458313";

--
-- Name: items_id_seq; Type: SEQUENCE; Schema: public; Owner: 458313
--

CREATE SEQUENCE public.items_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.items_id_seq OWNER TO "458313";

--
-- Name: items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: 458313
--

ALTER SEQUENCE public.items_id_seq OWNED BY public.items.id;


--
-- Name: student; Type: TABLE; Schema: public; Owner: 458313
--

CREATE TABLE public.student (
    username character varying(100) NOT NULL,
    student_id character varying(9) NOT NULL,
    name character varying(100) NOT NULL,
    class_id integer
);


ALTER TABLE public.student OWNER TO "458313";

--
-- Name: student_graded_items; Type: TABLE; Schema: public; Owner: 458313
--

CREATE TABLE public.student_graded_items (
    itemname character varying(50) NOT NULL,
    username character varying(50) NOT NULL,
    points integer
);


ALTER TABLE public.student_graded_items OWNER TO "458313";

--
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- Name: class class_id; Type: DEFAULT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.class ALTER COLUMN class_id SET DEFAULT nextval('public.class_class_id_seq'::regclass);


--
-- Name: items id; Type: DEFAULT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.items ALTER COLUMN id SET DEFAULT nextval('public.items_id_seq'::regclass);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: 458313
--

COPY public.categories (id, category_name, weight) FROM stdin;
1	midterm	15%
2	final	25%
3	project	15%
4	Quiz	10%
6	test	1%
\.


--
-- Data for Name: class; Type: TABLE DATA; Schema: public; Owner: 458313
--

COPY public.class (class_id, course_number, term, section_number, description) FROM stdin;
1	CS410	SP20	1	Databases
2	CS510	SP20	1	Databases for Graduates
3	CS321	FA20	1	Data-Structures
\.


--
-- Data for Name: items; Type: TABLE DATA; Schema: public; Owner: 458313
--

COPY public.items (id, itemname, category_name, description, point_value) FROM stdin;
1	midterm 1	midterm	First midterm of semester	100
2	Grades Database	project	First project of the semester	50
3	project 2	project	Second Project of the semester	50
4	Quiz 2	Quiz	Second quiz of the semester	10
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: 458313
--

COPY public.student (username, student_id, name, class_id) FROM stdin;
sarah123	2	Nera, Sarah	2
mary123	3	Cary, Mary	2
bEKSs123	4	Barry, Bell	2
bells123	5	Barry, Beksll	2
larry123	1	Wheels, Larry	2
\.


--
-- Data for Name: student_graded_items; Type: TABLE DATA; Schema: public; Owner: 458313
--

COPY public.student_graded_items (itemname, username, points) FROM stdin;
midterm 1	larry123	90
Quiz 2	larry123	12
\.


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: 458313
--

SELECT pg_catalog.setval('public.categories_id_seq', 6, true);


--
-- Name: class_class_id_seq; Type: SEQUENCE SET; Schema: public; Owner: 458313
--

SELECT pg_catalog.setval('public.class_class_id_seq', 3, true);


--
-- Name: items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: 458313
--

SELECT pg_catalog.setval('public.items_id_seq', 14, true);


--
-- Name: categories categories_category_name_key; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_category_name_key UNIQUE (category_name);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: class class_pkey; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.class
    ADD CONSTRAINT class_pkey PRIMARY KEY (class_id);


--
-- Name: items items_itemname_key; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_itemname_key UNIQUE (itemname);


--
-- Name: items items_pkey; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (id);


--
-- Name: student_graded_items student_graded_items_pkey; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student_graded_items
    ADD CONSTRAINT student_graded_items_pkey PRIMARY KEY (itemname, username);


--
-- Name: student student_pkey; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (student_id);


--
-- Name: student student_username_key; Type: CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_username_key UNIQUE (username);


--
-- Name: items items_category_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_category_name_fkey FOREIGN KEY (category_name) REFERENCES public.categories(category_name);


--
-- Name: student student_class_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_class_id_fkey FOREIGN KEY (class_id) REFERENCES public.class(class_id);


--
-- Name: student_graded_items student_graded_items_itemname_fkey; Type: FK CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student_graded_items
    ADD CONSTRAINT student_graded_items_itemname_fkey FOREIGN KEY (itemname) REFERENCES public.items(itemname);


--
-- Name: student_graded_items student_graded_items_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: 458313
--

ALTER TABLE ONLY public.student_graded_items
    ADD CONSTRAINT student_graded_items_username_fkey FOREIGN KEY (username) REFERENCES public.student(username);


--
-- PostgreSQL database dump complete
--

