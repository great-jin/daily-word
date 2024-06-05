<h1 align="center">Daily Word</h1>
Daily Word is english word recite application, and provide the friend competition.


## Support dictionary
- [X] CET4
- [X] CET6
- [X] GRE
- [X] Oxford
- [X] Graduate


## Use manual
Data forge current only support manual compile, include two part `daily-word-web` and `daily-word-server`.

### Backend-Server
Notice that backend must run with jdk 17.

Config the module `daily-word-server` of `application-dev.yml` to set the database connection.

The database script is stored in `document/sql/table_struct.sql` and `document/sql/table_data.sql`.


### Frontend-Web
The frontend web running step, default user `admin/123`.
```bash
cd daily-word-web

npm run i

npm run serve
```


## Todo List
- [ ] Support docker image quick deploy.
