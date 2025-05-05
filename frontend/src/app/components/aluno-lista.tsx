'use client';

import { useEffect, useState } from "react";
import {Aluno} from "@/app/types/aluno";
import {AlunoService} from "@/services/aluno-services";

export default function AlunoLista() {
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [carregando, setCarregando] = useState<boolean>(true);
    const [erro, setErro] = useState<string | null>(null);

    useEffect(() => {
        async function carregarAlunos() {
            try {
                setCarregando(true);
                const dados = await AlunoService.listarTodos();
                setAlunos(dados);
                setErro(null);
            } catch (error) {
                setErro('Falha ao carregar os alunos. Tente novamente mais tarde.');
                console.error(error);
            } finally {
                setCarregando(false);
            }
        }

        carregarAlunos();
    }, []);

    if (carregando) return <div>Carregando produtos...</div>;
    if (erro) return <div className="erro">{erro}</div>;

    return (
        <div>
            <h2>Lista de Alunos</h2>
            {alunos.length === 0 ? (
                <p>Nenhum aluno encontrado.</p>
            ) : (
                <ul className="aluno-lista">
                    {alunos.map((aluno) => (
                        <li key={aluno.id} className="aluno-item">
                            <h3>{aluno.nome}</h3>
                            <h3>{aluno.idade}</h3>

                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}