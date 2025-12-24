--
-- PostgreSQL database dump
--

\restrict rxOI6pcohwiiQ1cqX2WYKjxzRKhbgdxYjDnVONzO3SzN7o4JSv83QoQ78V3YAlS

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

-- Started on 2025-12-20 00:08:44

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 231 (class 1255 OID 17938)
-- Name: arac_durumunu_guncelle(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.arac_durumunu_guncelle() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE Tbl_Araclar
    SET Durum = 'Satıldı'
    WHERE AracID = NEW.AracID;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.arac_durumunu_guncelle() OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 17940)
-- Name: ciro_hesapla(date, date); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ciro_hesapla(baslangic date, bitis date) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
DECLARE
    toplam_ciro DECIMAL;
BEGIN
    SELECT COALESCE(SUM(SatisFiyati), 0) INTO toplam_ciro
    FROM Tbl_Satislar
    WHERE SatisTarihi BETWEEN baslangic AND bitis;
    RETURN toplam_ciro;
END;
$$;


ALTER FUNCTION public.ciro_hesapla(baslangic date, bitis date) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 228 (class 1259 OID 17898)
-- Name: tbl_araclar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_araclar (
    aracid integer NOT NULL,
    modelid integer,
    yil integer NOT NULL,
    fiyat numeric(18,2),
    plaka character varying(20),
    renk character varying(30),
    durum character varying(20) DEFAULT 'Satılık'::character varying,
    kilometre integer DEFAULT 0
);


ALTER TABLE public.tbl_araclar OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17897)
-- Name: tbl_araclar_aracid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_araclar_aracid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_araclar_aracid_seq OWNER TO postgres;

--
-- TOC entry 5081 (class 0 OID 0)
-- Dependencies: 227
-- Name: tbl_araclar_aracid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_araclar_aracid_seq OWNED BY public.tbl_araclar.aracid;


--
-- TOC entry 220 (class 1259 OID 17849)
-- Name: tbl_markalar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_markalar (
    markaid integer NOT NULL,
    markaad character varying(50) NOT NULL
);


ALTER TABLE public.tbl_markalar OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17848)
-- Name: tbl_markalar_markaid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_markalar_markaid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_markalar_markaid_seq OWNER TO postgres;

--
-- TOC entry 5082 (class 0 OID 0)
-- Dependencies: 219
-- Name: tbl_markalar_markaid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_markalar_markaid_seq OWNED BY public.tbl_markalar.markaid;


--
-- TOC entry 222 (class 1259 OID 17858)
-- Name: tbl_modeller; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_modeller (
    modelid integer NOT NULL,
    markaid integer,
    modelad character varying(50) NOT NULL
);


ALTER TABLE public.tbl_modeller OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17857)
-- Name: tbl_modeller_modelid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_modeller_modelid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_modeller_modelid_seq OWNER TO postgres;

--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 221
-- Name: tbl_modeller_modelid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_modeller_modelid_seq OWNED BY public.tbl_modeller.modelid;


--
-- TOC entry 226 (class 1259 OID 17885)
-- Name: tbl_musteriler; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_musteriler (
    musteriid integer NOT NULL,
    adsoyad character varying(100) NOT NULL,
    telefon character varying(15),
    tcno character varying(11),
    adres text
);


ALTER TABLE public.tbl_musteriler OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17884)
-- Name: tbl_musteriler_musteriid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_musteriler_musteriid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_musteriler_musteriid_seq OWNER TO postgres;

--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 225
-- Name: tbl_musteriler_musteriid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_musteriler_musteriid_seq OWNED BY public.tbl_musteriler.musteriid;


--
-- TOC entry 224 (class 1259 OID 17872)
-- Name: tbl_personel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_personel (
    personelid integer NOT NULL,
    kullaniciadi character varying(50) NOT NULL,
    sifre character varying(50) NOT NULL,
    yetki character varying(20),
    adsoyad character varying(50),
    CONSTRAINT tbl_personel_yetki_check CHECK (((yetki)::text = ANY ((ARRAY['Yonetici'::character varying, 'SatisTemsilcisi'::character varying])::text[])))
);


ALTER TABLE public.tbl_personel OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17871)
-- Name: tbl_personel_personelid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_personel_personelid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_personel_personelid_seq OWNER TO postgres;

--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 223
-- Name: tbl_personel_personelid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_personel_personelid_seq OWNED BY public.tbl_personel.personelid;


--
-- TOC entry 230 (class 1259 OID 17915)
-- Name: tbl_satislar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_satislar (
    satisid integer NOT NULL,
    aracid integer,
    musteriid integer,
    personelid integer,
    satistarihi timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    satisfiyati numeric(18,2)
);


ALTER TABLE public.tbl_satislar OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17914)
-- Name: tbl_satislar_satisid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_satislar_satisid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tbl_satislar_satisid_seq OWNER TO postgres;

--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 229
-- Name: tbl_satislar_satisid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_satislar_satisid_seq OWNED BY public.tbl_satislar.satisid;


--
-- TOC entry 4887 (class 2604 OID 17901)
-- Name: tbl_araclar aracid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_araclar ALTER COLUMN aracid SET DEFAULT nextval('public.tbl_araclar_aracid_seq'::regclass);


--
-- TOC entry 4883 (class 2604 OID 17852)
-- Name: tbl_markalar markaid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_markalar ALTER COLUMN markaid SET DEFAULT nextval('public.tbl_markalar_markaid_seq'::regclass);


--
-- TOC entry 4884 (class 2604 OID 17861)
-- Name: tbl_modeller modelid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_modeller ALTER COLUMN modelid SET DEFAULT nextval('public.tbl_modeller_modelid_seq'::regclass);


--
-- TOC entry 4886 (class 2604 OID 17888)
-- Name: tbl_musteriler musteriid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_musteriler ALTER COLUMN musteriid SET DEFAULT nextval('public.tbl_musteriler_musteriid_seq'::regclass);


--
-- TOC entry 4885 (class 2604 OID 17875)
-- Name: tbl_personel personelid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_personel ALTER COLUMN personelid SET DEFAULT nextval('public.tbl_personel_personelid_seq'::regclass);


--
-- TOC entry 4890 (class 2604 OID 17918)
-- Name: tbl_satislar satisid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_satislar ALTER COLUMN satisid SET DEFAULT nextval('public.tbl_satislar_satisid_seq'::regclass);


--
-- TOC entry 5073 (class 0 OID 17898)
-- Dependencies: 228
-- Data for Name: tbl_araclar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_araclar (aracid, modelid, yil, fiyat, plaka, renk, durum, kilometre) FROM stdin;
2	8	2001	410000.00	41 HF 796	GRI	Satılık	160000
3	6	2025	5400000.00	41 KML 1973	SIYAH	Satılık	6000
1	7	2019	1300000.00	34 CEL 632	BEYAZ	Satıldı	175000
\.


--
-- TOC entry 5065 (class 0 OID 17849)
-- Dependencies: 220
-- Data for Name: tbl_markalar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_markalar (markaid, markaad) FROM stdin;
1	Toyota
2	Honda
3	BMW
4	VW
5	Audi
6	Volgswagen
\.


--
-- TOC entry 5067 (class 0 OID 17858)
-- Dependencies: 222
-- Data for Name: tbl_modeller; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_modeller (modelid, markaid, modelad) FROM stdin;
1	1	Corolla
2	1	Yaris
3	2	Civic
4	3	320i
5	4	Polo
6	5	A6
7	1	Coralla
8	6	Polo
\.


--
-- TOC entry 5071 (class 0 OID 17885)
-- Dependencies: 226
-- Data for Name: tbl_musteriler; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_musteriler (musteriid, adsoyad, telefon, tcno, adres) FROM stdin;
1	Mehmet Yılmaz	5551234567	11111111111	Ankara/Çankaya
3	Berkay Tekin	5433059543	22222222222	Kocaeli/Izmit
\.


--
-- TOC entry 5069 (class 0 OID 17872)
-- Dependencies: 224
-- Data for Name: tbl_personel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_personel (personelid, kullaniciadi, sifre, yetki, adsoyad) FROM stdin;
1	admin	123	Yonetici	Ahmet Yönetici
2	satis	123	SatisTemsilcisi	Ayşe Satıcı
\.


--
-- TOC entry 5075 (class 0 OID 17915)
-- Dependencies: 230
-- Data for Name: tbl_satislar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_satislar (satisid, aracid, musteriid, personelid, satistarihi, satisfiyati) FROM stdin;
1	1	3	1	2025-12-19 23:45:59.940147	1300000.00
\.


--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 227
-- Name: tbl_araclar_aracid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_araclar_aracid_seq', 4, true);


--
-- TOC entry 5088 (class 0 OID 0)
-- Dependencies: 219
-- Name: tbl_markalar_markaid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_markalar_markaid_seq', 6, true);


--
-- TOC entry 5089 (class 0 OID 0)
-- Dependencies: 221
-- Name: tbl_modeller_modelid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_modeller_modelid_seq', 8, true);


--
-- TOC entry 5090 (class 0 OID 0)
-- Dependencies: 225
-- Name: tbl_musteriler_musteriid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_musteriler_musteriid_seq', 3, true);


--
-- TOC entry 5091 (class 0 OID 0)
-- Dependencies: 223
-- Name: tbl_personel_personelid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_personel_personelid_seq', 2, true);


--
-- TOC entry 5092 (class 0 OID 0)
-- Dependencies: 229
-- Name: tbl_satislar_satisid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_satislar_satisid_seq', 1, true);


--
-- TOC entry 4906 (class 2606 OID 17906)
-- Name: tbl_araclar tbl_araclar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_araclar
    ADD CONSTRAINT tbl_araclar_pkey PRIMARY KEY (aracid);


--
-- TOC entry 4908 (class 2606 OID 17908)
-- Name: tbl_araclar tbl_araclar_plaka_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_araclar
    ADD CONSTRAINT tbl_araclar_plaka_key UNIQUE (plaka);


--
-- TOC entry 4894 (class 2606 OID 17856)
-- Name: tbl_markalar tbl_markalar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_markalar
    ADD CONSTRAINT tbl_markalar_pkey PRIMARY KEY (markaid);


--
-- TOC entry 4896 (class 2606 OID 17865)
-- Name: tbl_modeller tbl_modeller_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_modeller
    ADD CONSTRAINT tbl_modeller_pkey PRIMARY KEY (modelid);


--
-- TOC entry 4902 (class 2606 OID 17894)
-- Name: tbl_musteriler tbl_musteriler_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_musteriler
    ADD CONSTRAINT tbl_musteriler_pkey PRIMARY KEY (musteriid);


--
-- TOC entry 4904 (class 2606 OID 17896)
-- Name: tbl_musteriler tbl_musteriler_tcno_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_musteriler
    ADD CONSTRAINT tbl_musteriler_tcno_key UNIQUE (tcno);


--
-- TOC entry 4898 (class 2606 OID 17883)
-- Name: tbl_personel tbl_personel_kullaniciadi_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_personel
    ADD CONSTRAINT tbl_personel_kullaniciadi_key UNIQUE (kullaniciadi);


--
-- TOC entry 4900 (class 2606 OID 17881)
-- Name: tbl_personel tbl_personel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_personel
    ADD CONSTRAINT tbl_personel_pkey PRIMARY KEY (personelid);


--
-- TOC entry 4910 (class 2606 OID 17922)
-- Name: tbl_satislar tbl_satislar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_satislar
    ADD CONSTRAINT tbl_satislar_pkey PRIMARY KEY (satisid);


--
-- TOC entry 4916 (class 2620 OID 17939)
-- Name: tbl_satislar trg_satis_sonrasi_stok; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_satis_sonrasi_stok AFTER INSERT ON public.tbl_satislar FOR EACH ROW EXECUTE FUNCTION public.arac_durumunu_guncelle();


--
-- TOC entry 4912 (class 2606 OID 17909)
-- Name: tbl_araclar tbl_araclar_modelid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_araclar
    ADD CONSTRAINT tbl_araclar_modelid_fkey FOREIGN KEY (modelid) REFERENCES public.tbl_modeller(modelid);


--
-- TOC entry 4911 (class 2606 OID 17866)
-- Name: tbl_modeller tbl_modeller_markaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_modeller
    ADD CONSTRAINT tbl_modeller_markaid_fkey FOREIGN KEY (markaid) REFERENCES public.tbl_markalar(markaid);


--
-- TOC entry 4913 (class 2606 OID 17923)
-- Name: tbl_satislar tbl_satislar_aracid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_satislar
    ADD CONSTRAINT tbl_satislar_aracid_fkey FOREIGN KEY (aracid) REFERENCES public.tbl_araclar(aracid);


--
-- TOC entry 4914 (class 2606 OID 17928)
-- Name: tbl_satislar tbl_satislar_musteriid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_satislar
    ADD CONSTRAINT tbl_satislar_musteriid_fkey FOREIGN KEY (musteriid) REFERENCES public.tbl_musteriler(musteriid);


--
-- TOC entry 4915 (class 2606 OID 17933)
-- Name: tbl_satislar tbl_satislar_personelid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_satislar
    ADD CONSTRAINT tbl_satislar_personelid_fkey FOREIGN KEY (personelid) REFERENCES public.tbl_personel(personelid);


-- Completed on 2025-12-20 00:08:44

--
-- PostgreSQL database dump complete
--

\unrestrict rxOI6pcohwiiQ1cqX2WYKjxzRKhbgdxYjDnVONzO3SzN7o4JSv83QoQ78V3YAlS

