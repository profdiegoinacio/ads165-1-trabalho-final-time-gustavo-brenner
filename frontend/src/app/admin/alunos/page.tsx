'use client';

import {useEffect, useState} from 'react';
import {Aluno} from "@/types/aluno";
import {AlunoService} from "@/services/aluno-services";
import AlunoForm from "@/components/aluno-form";

export default function AdminAlunos() {

    const [modoEdicao, setModoEdicao] = useState<boolean>(false);
    const [alunoSelecionado, setAlunoSelecionado] = useState<number | undefined>(undefined);
    const [atualizarLista, setAtualizarLista] = useState<number>(0);

    const handleNovoAluno = () => {
        setAlunoSelecionado(undefined);
        setModoEdicao(true);
    };

    const handleEditarAluno = (id: number) => {
        setAlunoSelecionado(id);
        setModoEdicao(true);
    };

    const handleExcluirAluno = async (id: number) => {
        if (window.confirm('Tem certeza que deseja excluir este aluno?')) {
            try {
                await AlunoService.excluir(id);
                // Atualiza a lista após a exclusão
                setAtualizarLista(prev => prev + 1);
            } catch (error) {
                console.error('Erro ao excluir aluno:', error);
                alert('Não foi possível excluir o aluno. Tente novamente.');
            }
        }
    };

    const handleSalvarAluno = () => {
        setModoEdicao(false);
        // Atualiza a lista para refletir as mudanças
        setAtualizarLista(prev => prev + 1);
    };

    const handleCancelar = () => {
        setModoEdicao(false);
    };

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-8">Gerenciamento de Alunos</h1>

            {modoEdicao ? (
                <AlunoForm
                    alunoId={alunoSelecionado}
                    onSalvar={handleSalvarAluno}
                    onCancelar={handleCancelar}
                />
            ) : (
                <div>
                    <button
                        className="mb-6 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        onClick={handleNovoAluno}
                    >
                        Novo Aluno
                    </button>

                    <AlunoListaAdmin
                        onEditar={handleEditarAluno}
                        onExcluir={handleExcluirAluno}
                        chaveAtualizacao={atualizarLista}
                    />
                </div>
            )}
        </div>
    );
}

// Componente de lista com ações administrativas
function AlunoListaAdmin({
                               onEditar,
                               onExcluir,
                               chaveAtualizacao
                           }: {
    onEditar: (id: number) => void;
    onExcluir: (id: number) => void;
    chaveAtualizacao: number;
}) {
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
                setErro('Falha ao carregar os alunos');
                console.error(error);
            } finally {
                setCarregando(false);
            }
        }

         carregarAlunos();
    }, [chaveAtualizacao]);


    if (carregando) return (
        <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
        </div>
    );

    if (erro) return (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6">
            <div className="flex">
                <div className="flex-shrink-0">
                    <svg className="h-5 w-5 text-red-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                         fill="currentColor" aria-hidden="true">
                        <path fillRule="evenodd"
                              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
                              clipRule="evenodd"/>
                    </svg>
                </div>
                <div className="ml-3">
                    <p className="text-sm text-red-700">{erro}</p>
                </div>
            </div>
        </div>
    );

    if (alunos.length === 0) {
        return (
            <div className="bg-white shadow overflow-hidden rounded-lg p-6 text-center">
                <p className="text-gray-500">Nenhum aluno cadastrado.</p>
                <p className="text-sm text-gray-400 mt-2">Clique em Novo Aluno para adicionar um aluno a lista.</p>
            </div>
        );
    }

    return (
        <div className="bg-white shadow overflow-hidden rounded-lg">
            <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th scope="col"
                            className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nome
                        </th>
                        <th scope="col"
                            className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Idade
                        </th>

                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {alunos.map((aluno) => (
                        <tr key={aluno.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">
                                <div className="text-sm font-medium text-gray-900">{aluno.nome}</div>
                            </td>
                            <td className="px-6 py-4">
                                <div className="text-sm text-gray-500 max-w-xs truncate">{aluno.idade}</div>
                            </td>


                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                <button
                                    onClick={() => onEditar(aluno.id!)}
                                    className="text-indigo-600 hover:text-indigo-900 mr-4"
                                >
                                    Editar
                                </button>
                                <button
                                    onClick={() => onExcluir(aluno.id!)}
                                    className="text-red-600 hover:text-red-900"
                                >
                                    Excluir
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}