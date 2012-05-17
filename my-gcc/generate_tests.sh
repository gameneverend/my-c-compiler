#!/bin/bash

if [ -e seperate_tests.sh ]
then
  rm seperate_tests.sh
fi

if [ -e tests/batterie_test.h ]
then
  rm tests/batterie_test.h
fi

if [ ! -e tests/singular_tests/asm ]
then
  mkdir tests/singular_tests/asm
fi

touch seperate_tests.sh
touch tests/batterie_test.h

PATH_1="tests/"
PATH_2=$PATH_1"singular_tests/"
PATH_3=$PATH_2"asm/"

echo "#!/bin/bash" >> seperate_tests.sh
echo "ant clean" >> seperate_tests.sh
echo "ant" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "OUR_COMPILER=\"java -jar jar/Compiler.jar\"" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "rm -f full" >> seperate_tests.sh
echo >> seperate_tests.sh
echo "#ifndef BATTERIE_TEST"  >> tests/batterie_test.h
echo "#define BATTERIE_TEST" >> tests/batterie_test.h
echo >> tests/batterie_test.h

i=0
END=.c
cd tests/singular_tests/
for entry in $(ls *$END)
do
  substring=${entry%$END}
  echo "TEST$i="$substring >> ../../seperate_tests.sh
  files[$i]="TEST$i"
  i=$(($i + 1))
  str=""
  for prototypes in `grep '^\<int\>' $entry | grep ';$'`
  do
		echo ${prototypes}
    if [[ "$prototypes" == *\; ]]
    then
      str="extern"$str" "$prototypes"\r"
			echo ${str}
      echo -e $str >> ../batterie_test.h
      str=""
    else
      str=$str" "$prototypes
    fi
  done
  echo -n $str >> ../batterie_test.h
  str=""
done
cd ../..

echo >> tests/batterie_test.h
echo "#endif" >> tests/batterie_test.h
echo >> seperate_tests.sh

i=0
for compile in ${files[@]}
do
  echo "if [ -e $PATH_2\$$compile.s ]" >> seperate_tests.sh
  echo "then" >> seperate_tests.sh
  echo "  rm $PATH_2\$$compile.s" >> seperate_tests.sh
  echo "fi" >> seperate_tests.sh
  echo "\$OUR_COMPILER $PATH_2\$$compile"$END >> seperate_tests.sh
  echo >> seperate_tests.sh
done

echo "mv $PATH_2*.s tests/singular_tests/asm/" >> seperate_tests.sh

echo >> seperate_tests.sh

echo -n gcc -g" " >> seperate_tests.sh
for gcc in ${files[@]}
do
  echo -n $PATH_3\$$gcc.s" " >> seperate_tests.sh
done
echo -n $PATH_1"full_test.c -o full" >> seperate_tests.sh

echo >> seperate_tests.sh

echo ./full >> seperate_tests.sh

chmod 755 seperate_tests.sh
