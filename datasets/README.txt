This package contains the WikiMID dataset in the form of tab separated values (TSV)
for the two languages EN and IT we provide a set of TSV files as follows:


- message-based_dataset.tsv
  a tab separated values list of:

  Id_user TAB Id_tweet TAB Id_interest TAB Interest_url

  +---------------+----------------------+-------------+---------------------------------------------------+
  | Id_user       | Id_tweet             | Id_interest | Interest_url                                      |
  +---------------+----------------------+-------------+---------------------------------------------------+
  | 1002342241    | 811616815474442241   |0001B        | https://www.goodreads.com/review/show/1845746853  |
  +---------------+----------------------+-------------+---------------------------------------------------+

  > 287296 id user

- message-based_interest_info.tsv
  a tab separated values list of:

  Id_interest TAB platform_type TAB Wiki_page

  +-------------+----------------+----------------------+
  | Id_interest | platform_type  | Wiki_page            |
  +-------------+----------------+----------------------+
  | 0001B       | Goodreads      | WIKI:EN:Animal_Farm  |
  +-------------+----------------+----------------------+


- friend-based_dataset.tsv
  a tab separated values list of "followOut" relations in the form:

  Id_user TAB Id_friend


  +-------------+-------------------------+
  | Id_user     | Id_friend               |  > 405282 id user
  +-------------+-------------------------+
  | 1002342241  | 1339835893              |  > 462683 id user + id friend
  +-------------+-------------------------+

- friend-based_interest_info.tsv
  a tab separated values list of relations in the form:

  Id_friend TAB Wiki_page

  +-------------+-------------------------+
  | Id_friend   | Wiki_page               | > 58789 righe
  +-------------+-------------------------+
  | 1339835893  | WIKI:EN:Hillary_Clinton |
  +-------------+-------------------------+


 n.b Id_friend sono identici
 > 444744 id user (message based) + id user (friend based)
 > 502135 id user (message based) + id user (friend based) + id friend (friend based)

 LICENSE
 All the datasets are licensed under a Creative Commons Attribution-Non Commercial-Share Alike 3.0  License: https://creativecommons.org/licenses/by-nc-sa/3.0/
