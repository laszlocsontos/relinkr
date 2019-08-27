INSERT INTO user_ (id,email_address,encrypted_password,time_zone,locale,confirmed,locked,created_date,last_modified_date,version_) VALUES (
156865797773164,'test@test.com',NULL,'516','en',false,false,'2019-06-19 19:32:08.473','2019-06-19 19:45:45.829',8);
INSERT INTO user_ (id,email_address,encrypted_password,time_zone,locale,confirmed,locked,created_date,last_modified_date,version_) VALUES (
158955547337177,'test2@test.com',NULL,'516','en',false,false,'2019-06-21 06:57:56.665','2019-07-25 12:43:26.100',26);

INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
156871917101687,'ACTIVE','https://start.spring.io/',NULL,NULL,NULL,NULL,NULL,'OmONvMD5pV',158955547337177,'2019-06-19 19:38:21.970','2019-06-19 19:38:21.970',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
156872880015656,'ACTIVE','https://start.spring.io/',NULL,NULL,NULL,NULL,NULL,'2XAln9QQ-G',158955547337177,'2019-06-19 19:39:20.742','2019-06-19 19:39:20.742',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
156877158004776,'ACTIVE','https://start.spring.io/',NULL,NULL,NULL,NULL,NULL,'yE9Be1d1-p',158955547337177,'2019-06-20 19:43:41.849','2019-06-20 19:43:41.849',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
156878757313187,'ACTIVE','https://start.spring.io/',NULL,NULL,NULL,NULL,NULL,'w8l-Bx5wQE',158955547337177,'2019-06-20 19:45:19.463','2019-06-20 19:45:19.463',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
156879450367275,'ACTIVE','https://start.spring.io/',NULL,NULL,NULL,NULL,NULL,'5XG-Ye2e5J',158955547337177,'2019-06-21 19:46:01.764','2019-06-21 19:46:01.764',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
159054940004394,'ACTIVE','https://goodapi.co/blog/rest-vs-graphql',NULL,NULL,NULL,NULL,NULL,'ExGwyRqmjr',158955547337177,'2019-06-21 08:39:03.114','2019-06-21 08:39:03.114',0);
INSERT INTO link (id,link_status,long_url,utm_campaign,utm_content,utm_medium,utm_source,utm_term,path_,user_id,created_date,last_modified_date,version_) VALUES (
160468778190878,'ACTIVE','https://console.cloud.google.com/appengine/versions?project=craftingjava-sbcc&serviceId=default&versionssize=50','s','','s','s','','MEMdXwWx96',158955547337177,'2019-06-22 08:37:16.950','2019-06-22 08:37:16.950',0);

INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685195,159054940004394,'2019-06-19',19,6,6,6,'2019-06-19 06:47:07.979',160360496661871,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685196,159054940004394,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661872,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685197,159054940004394,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661873,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685198,156871917101687,'2019-06-19',19,6,6,6,'2019-06-19 06:47:07.979',160360496661874,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685199,156871917101687,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661875,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685200,156872880015656,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661876,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685201,156872880015656,'2019-06-22',22,6,6,6,'2019-06-22 06:47:07.979',160360496661877,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685202,156877158004776,'2019-06-21',21,6,6,6,'2019-06-21 06:47:07.979',160360496661877,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685203,156877158004776,'2019-06-21',21,6,6,6,'2019-06-21 06:47:07.979',160360496661872,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685204,156877158004776,'2019-06-19',19,6,6,6,'2019-06-19 06:47:07.979',160360496661873,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685205,156877158004776,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661874,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685206,156879450367275,'2019-06-20',20,6,6,6,'2019-06-20 06:47:07.979',160360496661875,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685207,159054940004394,'2019-06-22',22,6,6,6,'2019-06-22 06:47:07.979',160360496661876,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685208,160468778190878,'2019-06-21',21,6,6,6,'2019-06-21 06:47:07.979',160360496661877,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);
INSERT INTO click (id,link_id,visit_date,visit_day_of_month,visit_day_of_week,visit_hour,visit_month,visit_timestamp,visitor_id,user_id,ip_address,ip_address_type,country,created_date,last_modified_date,version_) VALUES (
160360505685209,160468778190878,'2019-06-21',21,6,6,6,'2019-06-21 06:47:07.979',160360496661877,158955547337177,'0:0:0:0:0:0:0:0',1,NULL,'2019-06-22 06:47:08.520','2019-06-22 06:47:08.520',0);

INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661871,'2019-06-19 06:47:07.969','2019-06-19 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661872,'2019-06-19 06:47:07.969','2019-06-19 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661873,'2019-06-19 06:47:07.969','2019-06-19 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661874,'2019-06-20 06:47:07.969','2019-06-20 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661875,'2019-06-20 06:47:07.969','2019-06-20 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661876,'2019-06-21 06:47:07.969','2019-06-21 06:47:07.969',0);
INSERT INTO visitor (id,created_date,last_modified_date,version_) VALUES (
160360496661877,'2019-06-22 06:47:07.969','2019-06-22 06:47:07.969',0);
