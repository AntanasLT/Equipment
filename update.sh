#!/bin/bash
dist="/home/a/Programme/java/berufliche/Equipment/dist"
iranga="/home/a/Programme/java/berufliche/Equipment/Iranga"
zurnalaP="/home/a/Programme/java/berufliche/Equipment/ZurnalaP"
zurnalaS="/home/a/Programme/java/berufliche/Equipment/ZurnalaS"

function a () {
	rm -rf $dest/*
	cp -R $dist/* $dest/
#	if [[ `mount|grep 'OneDrive'` != "" ]]; then
#		b="Programme/java/berufliche/Equipment/$config"
#		rsync -avu --delete /home/a/$b/ /home/a/OneDrive/$b; echo `date`': /home/a/'"$b/" >> /home/a/OneDrive/letzte.txt
#	else
#		echo 'Καμιά σύνδεση με Ονε δρίωε'
#	fi
}

read -p 'I – η εφαρμογή μου
P – η εφαρμογή των μηχανικών βαρδιάς
S – η εφαρμογή τησ Σάνδρας: ' a
echo

case $a in
	I) dest=$iranga; config="Iranga"; a;;
	P) dest=$zurnalaP; config="ZurnalaP"; a;;
	S) dest=$zurnalaS; config="ZurnalaS"; a;;
esac
